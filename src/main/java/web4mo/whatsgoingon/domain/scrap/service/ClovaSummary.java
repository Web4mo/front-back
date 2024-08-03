package web4mo.whatsgoingon.domain.scrap.service;
/*
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class ClovaSummary {

    //@Value("${spring.summary.id}")
    private final String id;

    //@Value("${spring.summary.secret}")
    private final String secret;


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

 */
