package web4mo.whatsgoingon.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web4mo.whatsgoingon.domain.article.entity.Article;
import web4mo.whatsgoingon.domain.article.entity.ArticleContent;

public interface ArticleContentRepository extends JpaRepository<ArticleContent, Long> {
    ArticleContent findByArticle(Article article);
}
