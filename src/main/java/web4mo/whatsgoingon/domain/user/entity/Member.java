package web4mo.whatsgoingon.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import web4mo.whatsgoingon.domain.BaseDate;


import java.net.URL;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 30, nullable = false)
    private Long id;

    @Column
    private String loginId;

    @Column
    private String password;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private URL profileImg;

    @Column
    private String userType;

    public void updateProfilImg(URL newUrl){
        this. profileImg=newUrl;
    }

    public void updatePassword(String password){
        this.password=password;
    }
    public void updateUserType(String userType){
        this.userType=userType;
    }
}
