package web4mo.whatsgoingon.domain.scrap.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScrapResponseDto {
    private Long scrapId;
    private String title;
    private String modifiedAt;
}
