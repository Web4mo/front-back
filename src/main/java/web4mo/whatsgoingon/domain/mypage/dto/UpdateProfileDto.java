package web4mo.whatsgoingon.domain.mypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import web4mo.whatsgoingon.domain.category.entity.Media;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {
    private String userType;
    private List<String> userCategories;
    private List<String> userKeywords;
    private Map<String, String> userMedium;
}
