package web4mo.whatsgoingon.config.NaverApi;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ArticleApiDto {
    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
}
