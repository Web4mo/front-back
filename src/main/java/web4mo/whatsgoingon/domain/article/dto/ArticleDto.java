package web4mo.whatsgoingon.domain.article.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web4mo.whatsgoingon.domain.category.entity.Category;
//import web4mo.whatsgoingon.domain.category.entity.UserCategoryKeyword;
//import web4mo.whatsgoingon.domain.category.entity.Keyword;

import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {

    private Long id;
    private String title;
    private String url;
    private LocalDate pubDate;
    private boolean crawling;
    private String img;
    //private UserCategoryKeyword category;
}