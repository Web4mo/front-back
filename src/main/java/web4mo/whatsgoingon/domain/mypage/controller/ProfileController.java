package web4mo.whatsgoingon.domain.mypage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import web4mo.whatsgoingon.domain.category.entity.Category;
import web4mo.whatsgoingon.domain.category.entity.Media;
import web4mo.whatsgoingon.domain.mypage.dto.EditPasswordDto;
import web4mo.whatsgoingon.domain.mypage.dto.ProfileDto;
import web4mo.whatsgoingon.domain.mypage.dto.UpdateProfileDto;
import web4mo.whatsgoingon.domain.mypage.service.ProfileService;
import web4mo.whatsgoingon.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static web4mo.whatsgoingon.response.Message.*;
import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "Profile Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Transactional
    @GetMapping("/getProfile")
    @ResponseStatus(OK)
    public Response getProfile() {
        ProfileDto profileDTO = profileService.getProfile();
        return success(FETCH_PROFILE, profileDTO);
    }

    @PutMapping("/updateProfile")
    @ResponseStatus(OK)
    @Transactional
    public Response updateProfile(@RequestBody UpdateProfileDto updateProfileDto) {
        ProfileDto updatedProfile = profileService.updateProfile(updateProfileDto);
        return success(UPDATE_PROFILE, updatedProfile);
    }

    @PutMapping("/editpassword")
    @ResponseStatus(OK)
    @Transactional
    public Response editPassword(@RequestBody EditPasswordDto editPasswordDto){
        ProfileDto updatedProfile = profileService.editPassword(editPasswordDto);
        return success(EDIT_PASSWORD, updatedProfile);
    }


    @PostMapping(consumes = { "multipart/form-data" })
    @ResponseStatus(OK)
    public Response uploadImage(@RequestParam("image") MultipartFile image,
                                @RequestParam("dir") String dir) {
        try {
            // 프로필 이미지 업로드 및 업데이트 처리
            ProfileDto uploadImage = profileService.uploadImage(image, dir);
            // 성공적으로 처리되었음을 응답
            return success("프로필 이미지 업로드 성공", uploadImage);
        } catch (IOException e) {
            // 예외 발생 시 클라이언트에게 오류 메시지 반환
            return success("프로필 이미지 업로드 중 오류가 발생했습니다.", e.getMessage());
        }
    }

    @DeleteMapping("/")
    @ResponseStatus(OK)
    public Response deleteImage() throws IOException {
        ProfileDto deleteImage = profileService.deleteImage();
        return success(DELETE_IMG, deleteImage);
    }

}
