package web4mo.whatsgoingon.domain.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private String userId;
}
