package web4mo.whatsgoingon.domain.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class MediaSelectionDto {
    private String userId;
    private Map<String, String> medias;

    @Builder
    public MediaSelectionDto(String userId,Map<String, String> medias){
        this.userId=userId;
        this.medias=medias;
    }
}

