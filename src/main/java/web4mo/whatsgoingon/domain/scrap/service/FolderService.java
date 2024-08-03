package web4mo.whatsgoingon.domain.scrap.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.scrap.dto.FolderResponseDto;
import web4mo.whatsgoingon.domain.scrap.dto.ScrapResponseDto;
import web4mo.whatsgoingon.domain.scrap.entity.Folder;
import web4mo.whatsgoingon.domain.scrap.entity.Scrap;
import web4mo.whatsgoingon.domain.scrap.repository.FolderRepository;
import web4mo.whatsgoingon.domain.scrap.repository.ScrapRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final ScrapRepository scrapRepository;
    private final UserService userService;

    public List<FolderResponseDto> getFolderList(String sortingMethod){
        Member member = userService.getCurrentMember();
        List<Folder> folderList;
        List<FolderResponseDto> response = new ArrayList<>();

        if(sortingMethod.equals("이름")){ folderList = folderRepository.findByMemberOrderByName(member);  }
        else if(sortingMethod.equals("날짜")){ folderList = folderRepository.findByMemberOrderByModifiedAt(member); }
        else { folderList = folderRepository.findByMember(member); }

        for(Folder i : folderList){
            response.add(FolderResponseDto.builder()
                    .folderId(i.getId())
                    .folderName(i.getName())
                    .lastModifiedAt(i.getModifiedAt())
                    .build());
        }
        return response;
    }

    public Long addFolder(String folderName) {
        Member member = userService.getCurrentMember();
        Folder folder = Folder.builder()
                .member(member)
                .name(folderName).build();
        Long folderId = folder.getId();
        folderRepository.save(folder);
        return folderId;
    }

    public void editFolderName(Long folderId, String folderName) {
        // 자기 폴더 수정하는지 확인 작업 필요
        Folder folder = folderRepository.findAById(folderId);

        folder.updateName(folderName);
    }

    public void deleteFolder(Long folderId) {
        // 자기 폴더 수정하는지 확인 작업 필요
        Folder folder = folderRepository.findAById(folderId);
        if(folder == null){
            return;
        }
        folderRepository.delete(folder);
    }

    public List<ScrapResponseDto> scrapList(Long folderId) {
        // 정렬 방법 추가 필요
        List<Scrap> scrapList = scrapRepository.findByFolderId(folderId);
        List<ScrapResponseDto> scrapResponseDtoList = new ArrayList<>();

        for(Scrap scrap : scrapList){
            scrapResponseDtoList.add(ScrapResponseDto.builder()
                    .scrapId(scrap.getId())
                    .title(scrap.getArticle().getTitle())
                    .modifiedAt(scrap.getModifiedAt()).build());
        }

        return scrapResponseDtoList;
    }
}
