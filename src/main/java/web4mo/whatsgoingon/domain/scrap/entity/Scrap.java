package web4mo.whatsgoingon.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web4mo.whatsgoingon.domain.BaseTime;
import web4mo.whatsgoingon.domain.article.entity.Article;


@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scrap extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id")
    private Folder folder;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(columnDefinition = "TEXT")
    private String articleSummary;

    public void memoUpdate(String memo){
        this.memo = memo;
    }
    public void aiUpdate(String ai){
        this.articleSummary = ai;
    }
}
