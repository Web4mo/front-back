package web4mo.whatsgoingon.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@Builder
public class GetMemberTestDto {
    private String loginId;
    private String password;
}
