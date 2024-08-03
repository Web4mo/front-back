package web4mo.whatsgoingon.domain.scrap.dto;

import lombok.Builder;
import lombok.Getter;
//import web4mo.whatsgoingon.domain.category.entity.UserCategoryKeyword;


import java.time.LocalDate;

@Getter
@Builder
public class ScrapPageDto {
    private String url;
    //private UserCategoryKeyword category;
    private LocalDate pubDate;
    private String content;
    private String memo;
    private String ai;
}
