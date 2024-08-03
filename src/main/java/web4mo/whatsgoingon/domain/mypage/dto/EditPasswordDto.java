package web4mo.whatsgoingon.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditPasswordDto {
    String currentPassword;
    String newPassword;
    String confirmPassword;
}
