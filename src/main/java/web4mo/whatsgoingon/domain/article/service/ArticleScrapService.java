package web4mo.whatsgoingon.domain.article.service;


import lombok.AllArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.article.entity.Article;
import web4mo.whatsgoingon.domain.article.entity.ArticleContent;
import web4mo.whatsgoingon.domain.article.repository.ArticleContentRepository;
import web4mo.whatsgoingon.domain.article.repository.ArticleRepository;
import web4mo.whatsgoingon.domain.scrap.entity.Scrap;
import web4mo.whatsgoingon.domain.scrap.repository.FolderRepository;
import web4mo.whatsgoingon.domain.scrap.repository.ScrapRepository;
import web4mo.whatsgoingon.exception.TestException;

import java.io.IOException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleScrapService {
    ArticleRepository articleRepository;
    FolderRepository folderRepository;
    ScrapRepository scrapRepository;
    ArticleContentRepository articleContentRepository;

    public void scraping(Long articleId, Long folderId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        if(!article.isCrawling()){
            try {
                article.updateCrawling();
                String content = makeContent(article.getUrl());
                articleContentRepository.save(ArticleContent.builder()
                        .article(article)
                        .content(content).build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        folderRepository.findById(folderId);
        Scrap scrap = Scrap.builder()
                .article(articleRepository.findAById(articleId))
                .folder(folderRepository.findAById(folderId)).build();
        scrapRepository.save(scrap);
    }

    public String makeContent(String url) throws IOException {
        Connection articleConn = Jsoup.connect(url).userAgent("Mozilla/5.0"); // 네이버 뉴스 기사 페이지 요청
        Document articleDoc = articleConn.get();

        Element content = articleDoc.selectFirst("#dic_area");  // 기사 본문 추출
        if (content != null) {
            return content.text();
        } else {
            return "기사 본문을 찾을 수 없습니다.";
        }
        //여기도 주석
    }
}
