package web4mo.whatsgoingon.domain.mypage.service;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.category.service.CategoryKeywordService;
import web4mo.whatsgoingon.domain.mypage.dto.EditPasswordDto;
import web4mo.whatsgoingon.domain.mypage.dto.ProfileDto;
import web4mo.whatsgoingon.domain.mypage.dto.UpdateProfileDto;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.repository.UserRepository;
import web4mo.whatsgoingon.domain.user.service.UserService;

import java.io.IOException;
import java.net.URL;

@Service
@AllArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final CategoryKeywordService categoryKeywordService;
    private final S3Uploader s3Uploader;

    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ProfileDto getProfile() {
        Member member = userService.getCurrentMember(); // 현재 로그인되어있는 정보
        return ProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .userType(member.getUserType())
                .createAt(member.getCreatedAt())
                .interests(categoryKeywordService.userCategories(member))
                .keywords(categoryKeywordService.userKeywords(member))
                .media(categoryKeywordService.userMedium(member))
                .profileImg(member.getProfileImg())
                .build();
    }

    @Transactional
    public ProfileDto updateProfile(UpdateProfileDto updateProfileDto){
        Member member = userService.getCurrentMember(); // 현재 로그인되어있는 정보
        if (member.getUserType() != null) {
            member.updateUserType(updateProfileDto.getUserType());
        }
        if (updateProfileDto.getUserCategories() != null) {
            categoryKeywordService.updateUserCategories(member, updateProfileDto.getUserCategories());
        }
        if (updateProfileDto.getUserKeywords() != null) {
            categoryKeywordService.updateUserKeywords(member, updateProfileDto.getUserKeywords());
        }
        if (updateProfileDto.getUserMedium() != null) {
            categoryKeywordService.updateUserMedium(member, updateProfileDto.getUserMedium());
        }

        return ProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .userType(member.getUserType())
                .createAt(member.getCreatedAt())
                .interests(categoryKeywordService.userCategories(member))
                .keywords(categoryKeywordService.userKeywords(member))
                .media(categoryKeywordService.userMedium(member))
                .profileImg(member.getProfileImg())
                .build();
    }

    public ProfileDto editPassword(EditPasswordDto profileDto){
        Member member = userService.getCurrentMember(); // 현재 로그인되어있는 정보

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(profileDto.getCurrentPassword(), passwordEncoder.encode(member.getPassword()))) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새로운 비밀번호 확인
        if (!profileDto.getNewPassword().equals(profileDto.getConfirmPassword())) {
            throw new IllegalArgumentException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 비밀번호 업데이트
        member.updatePassword(profileDto.getNewPassword()); // 비밀번호 인코딩
        userRepository.save(member);// 회원 정보 업데이트

        return ProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .userType(member.getUserType())
                .createAt(member.getCreatedAt())
                .interests(categoryKeywordService.userCategories(member))
                .keywords(categoryKeywordService.userKeywords(member))
                .media(categoryKeywordService.userMedium(member))
                .profileImg(member.getProfileImg())
                .build();
    }

    //이미지 업로드 및 수정
    public ProfileDto uploadImage(MultipartFile image,String dir) throws IOException {
        if(image.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }
        Member member = userService.getCurrentMember();
        String oldfilename = String.valueOf(member.getProfileImg());
        URL newURL=new URL(s3Uploader.updateFile(image, oldfilename,dir));
        member.updateProfilImg(newURL);
        userRepository.save(member);

        return ProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .loginId(member.getLoginId())
                .userType(member.getUserType())
                .createAt(member.getCreatedAt())
                .interests(null)
                .keywords(null)
                .media(null)
                .profileImg(member.getProfileImg())
                .build();
    }


    // 이미지 삭제 로직
    public ProfileDto deleteImage() throws IOException {
        Member member = userService.getCurrentMember();
        String oldfilename = String.valueOf(member.getProfileImg());
        s3Uploader.deleteFile(oldfilename);
        member.updateProfilImg(null);
        userRepository.save(member);
        return null;
    }

}
