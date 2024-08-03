//package web4mo.whatsgoingon.config;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//
//@Service
//public class CrawlingService {
//
//    public String fetchArticleContent(String url) throws IOException {
//        Connection articleConn = Jsoup.connect(url).userAgent("Mozilla/5.0");
//        Document articleDoc = articleConn.get();
//
//        Element content = articleDoc.selectFirst("#dic_area");
//        if (content != null) {
//            return content.text();
//        } else {
//            return "기사 본문을 찾을 수 없습니다.";
//        }
//    }
//}