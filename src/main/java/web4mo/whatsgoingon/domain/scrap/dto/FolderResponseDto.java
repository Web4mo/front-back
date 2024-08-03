package web4mo.whatsgoingon.domain.scrap.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FolderResponseDto {
    private Long folderId;
    private String folderName;
    private String lastModifiedAt;
}
