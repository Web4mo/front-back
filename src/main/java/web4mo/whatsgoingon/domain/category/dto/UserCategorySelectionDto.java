package web4mo.whatsgoingon.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import web4mo.whatsgoingon.domain.category.entity.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor

public class UserCategorySelectionDto {
    private String userId;
    private List<String> category;
    private List<String> keyword;

    @Builder
    public UserCategorySelectionDto(String userId,List<String> category,List<String> keyword ) {
        this.userId = userId;
        this.category=category;
        this.keyword=keyword;
    }
}

