package web4mo.whatsgoingon.domain.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import web4mo.whatsgoingon.config.Authentication.JwtAuthenticationFilter;
import web4mo.whatsgoingon.config.Authentication.JwtTokenProvider;
import web4mo.whatsgoingon.domain.scrap.entity.Folder;
import web4mo.whatsgoingon.domain.scrap.repository.FolderRepository;
import web4mo.whatsgoingon.domain.user.dto.LogInRequestDto;
import web4mo.whatsgoingon.domain.user.dto.SignUpRequestDto;
import web4mo.whatsgoingon.domain.user.dto.TokenDto;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.entity.RefreshToken;
import web4mo.whatsgoingon.domain.user.repository.RefreshTokenRepository;
import web4mo.whatsgoingon.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final FolderRepository folderRepository;

    /*
    *회원가입
     */
    @Transactional
    public Member signup(SignUpRequestDto signUpRequestDto){
        validateDuplicateUser(signUpRequestDto.getLoginId());
        isPasswordMatching(signUpRequestDto.getPassword(), signUpRequestDto.getConfirmPassword());

        Member member = signUpRequestDto.toEntity();
        Folder folder = Folder.builder()
                .member(member)
                .name("기본 폴더").build();

        folderRepository.save(folder);
        userRepository.save(member);
        return userRepository.save(member);
    }

    //loginId 중복 체크
    private void validateDuplicateUser(String loginId) {
        if (userRepository.existsByLoginId(loginId)){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //비밀번호 일치 확인
    private void isPasswordMatching(String password1, String password2){
        if (password1 == null){
            throw new IllegalStateException("비밀번호가 없습니다.");
        }
        if( !password1.equals(password2)){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    /*
     *로그인
     */
    @Transactional
    public TokenDto login(LogInRequestDto logInRequestDto){
        Optional<Member> optionalUser = userRepository.findByLoginId(logInRequestDto.getLoginId());
        log.info("로그인 진행중 ....");
        if(optionalUser.isEmpty()){
            throw new IllegalStateException("회원이 아닙니다.");
        }
        Member member =optionalUser.get();
        if(!member.getPassword().equals(logInRequestDto.getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(logInRequestDto.getLoginId(),logInRequestDto.getPassword());
         // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication=authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if(!authentication.isAuthenticated())
            log.info("인증 실패");

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String userId= authentication.getName();

        //인증 정보 기반으로 jwt 토큰 생성
        TokenDto tokenDto=jwtTokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken= RefreshToken.builder()
                .userId(logInRequestDto.getLoginId())
                .refreshToken(tokenDto.getRefreshToken())
                .grantAuthority(authentication.getAuthorities().toString()).build();
        if (! refreshTokenRepository.existsByUserId(userId)){
            refreshTokenRepository.save(refreshToken);
        }
        else {
            refreshTokenRepository.findByUserId(userId).updateRefreshToken(tokenDto.getRefreshToken());
        }
        return tokenDto;
    }

    //access, refresh 토큰 재발급
    @Transactional
    public TokenDto tokenReissue(TokenDto tokenDto){
        String refreshToken =tokenDto.getRefreshToken();
        Authentication authentication=jwtTokenProvider.getAuthentication(refreshToken);
        String userId= authentication.getName();

        if(StringUtils.hasText(refreshToken ) && jwtTokenProvider.validateToken(refreshToken)){
            log.info("getting new access Token");
            String newAccessToken = jwtTokenProvider.createAccessToken(authentication);
            return TokenDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(refreshToken)
                    .userId(userId)
                    .build();
        }else { //refresh token 만료
            refreshTokenRepository.deleteByUserId(userId);
            //RT 만료됐다는걸 알리는 예외 발생 ->  home으로 유도
            throw new IllegalStateException("Refresh token 만료됨");
        }
    }

    //로그아웃
    @Transactional
    public void logout(String token){

        log.info(getCurrentMember().getLoginId()+"이 로그아웃 중입니다.");
        SecurityContextHolder.clearContext();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization", "");

    }

    //회원 찾기
    public Member getCurrentMember() {

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if( authentication==null||!authentication.isAuthenticated()){
            throw new RuntimeException("no authicated user found");
        }
        Optional<Member> member=userRepository.findByLoginId(authentication.getName());
        if(member.isEmpty()) {
            throw new IllegalStateException("회원이 없습니다.");
        }
        return member.get();
    }

    //전체 회원 조회
    public List<String> findAll(){
        List<Member> members=userRepository.findAllBy();
        List<String> memberList= new ArrayList<>();
        for(Member member: members){
            memberList.add(member.getLoginId());
        }
        return memberList;
    }

//    public void updateProfile(Member member) {    }
//    public void editPassword(Member member) {    }
}
