package web4mo.whatsgoingon.domain.mypage.dto;

import lombok.*;
import web4mo.whatsgoingon.domain.category.entity.Category;
import web4mo.whatsgoingon.domain.category.entity.Media;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProfileDto {
    private Long id;
    private String name;
    private String loginId;
    private String currentPassword; // 현재 비밀번호
    private String newPassword; // 새로운 비밀번호
    private String confirmPassword; // 새로운 비밀번호 확인
    private String userType;
    private String createAt; //assignDate
    private List<Category> interests;
    private List<String> keywords;
    private List<String> media;
    private URL profileImg;
}
