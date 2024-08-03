package web4mo.whatsgoingon.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.entity.Role;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String loginId;
    private String password;
    private String confirmPassword;
    private String name;
    private String userType;


    @Builder
    public SignUpRequestDto(String loginId, String password, String confirmPassword, String name, String userType){
        this.loginId=loginId;
        this.password=password;
        this.confirmPassword=confirmPassword;
        this.name=name;
        this.userType=userType;
    }

    public Member toEntity(){
        return Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .userType(userType)
                .role(Role.User)
                .build();
    }
}
