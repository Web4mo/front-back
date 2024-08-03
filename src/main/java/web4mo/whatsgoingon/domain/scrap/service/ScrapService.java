package web4mo.whatsgoingon.domain.scrap.service;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.article.entity.Article;
import web4mo.whatsgoingon.domain.article.entity.ArticleContent;
import web4mo.whatsgoingon.domain.article.repository.ArticleContentRepository;
import web4mo.whatsgoingon.domain.article.repository.ArticleRepository;
import web4mo.whatsgoingon.domain.scrap.dto.FolderResponseDto;
import web4mo.whatsgoingon.domain.scrap.dto.ScrapPageDto;
import web4mo.whatsgoingon.domain.scrap.entity.Folder;
import web4mo.whatsgoingon.domain.scrap.entity.Scrap;
import web4mo.whatsgoingon.domain.scrap.repository.FolderRepository;
import web4mo.whatsgoingon.domain.scrap.repository.ScrapRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final FolderRepository folderRepository;
    private final ArticleRepository articleRepository;
    private final ArticleContentRepository articleContentRepository;
    private final UserService userService;

    public List<FolderResponseDto> clickScrap() {
        Member member = userService.getCurrentMember();
        List<Folder> folderList;
        List<FolderResponseDto> response = new ArrayList<>();

        folderList = folderRepository.findByMemberOrderByName(member);
        for(Folder i : folderList){
            response.add(FolderResponseDto.builder()
                    .folderId(i.getId())
                    .folderName(i.getName())
                    .lastModifiedAt(i.getModifiedAt())
                    .build());
        }
        return response;
    }


    public Long scrapMain(Long articleId, Long folderId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        Folder folder = folderRepository.findAById(folderId);
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
        Scrap scrap = Scrap.builder()
                .folder(folder)
                .article(article).build();
        scrapRepository.save(scrap);
        return scrap.getId();

    }
    public ScrapPageDto scrapPage(Long scrapId) {
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow();
        Article article = scrap.getArticle();
        ArticleContent articleContent = articleContentRepository.findByArticle(article);
        ScrapPageDto scrapPageDto = ScrapPageDto.builder()
                .url(article.getUrl())
                //.category(article.getKeyword())
                .pubDate(article.getPubDate())
                .content(articleContent.getContent())
                .memo(scrap.getMemo())
                .ai(scrap.getArticleSummary()).build();
        return scrapPageDto;
    }
    public void deleteScrap(Long scrapId) {
        scrapRepository.delete(scrapRepository.findById(scrapId).orElseThrow());
    }

    public void scrapMemo(Long scrapId, String memo) {
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow();
        scrap.memoUpdate(memo);
    }

    public String scrapAI(Long scrapId) {
        Scrap scrap = scrapRepository.findById(scrapId).orElseThrow();
        String content = articleContentRepository.findByArticle(scrap.getArticle()).getContent();
        String aiSummary = makeSummary(content);
        scrap.aiUpdate(aiSummary);
        return aiSummary;

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
    }
    public String makeSummary(String content){

        String language = "ko";
        String model = "news";
        String tone = "2";
        String summaryCount = "3";
        String url = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize";
        String title = "'하루 2000억' 판 커지는 간편송금 시장";

        String summaryResult = "";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "z99hax7em9");
            con.setRequestProperty("X-NCP-APIGW-API-KEY", "JuJ1J9KUTfOFkMdiXlPrMRVdAcnow8lkvO63Y2uh");

            JSONObject document = new JSONObject();
            document.put("title", title);
            document.put("content", content);

            JSONObject option = new JSONObject();
            option.put("language", language);
            option.put("model", model);
            option.put("tone", tone);
            option.put("summaryCount", summaryCount);

            JSONObject data = new JSONObject();
            data.put("document", document);
            data.put("option", option);

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(data.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 결과를 String에 담기
                summaryResult = response.toString();
                return summaryResult;
                //System.out.println("Summary Result: " + summaryResult);
            } else {
                return "AI 요약 실패 . . .";
                //System.out.println("Error: " + con.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "summaryResult";
    }


}
