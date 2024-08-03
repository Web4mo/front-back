package web4mo.whatsgoingon.domain.article.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web4mo.whatsgoingon.domain.article.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article findAById(Long articleId);
}
