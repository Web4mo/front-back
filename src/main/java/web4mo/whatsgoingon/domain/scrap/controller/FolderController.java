package web4mo.whatsgoingon.domain.scrap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.domain.scrap.dto.FolderResponseDto;
import web4mo.whatsgoingon.domain.scrap.dto.ScrapResponseDto;
import web4mo.whatsgoingon.domain.scrap.service.FolderService;
import web4mo.whatsgoingon.response.Response;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static web4mo.whatsgoingon.response.Message.*;
import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "Folder Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping(value="/mypage")
public class FolderController {
    private final FolderService folderService;

    @PostMapping("/scrapList/addFolder")
    @ResponseStatus(OK)
    public Response addFolder(@RequestParam(value = "FolderName", required = false, defaultValue = "새 폴더") String string)
    {
        Long folderId = folderService.addFolder(string);
        return success(ADD_FOLDER, folderId);
    }

    @GetMapping("/scrapList")
    @ResponseStatus(OK)
    public Response getFolderList(@RequestParam(value = "sortingMethod", required = false, defaultValue = "이름") String sortingMethod)
    {
        List<FolderResponseDto> response = folderService.getFolderList(sortingMethod);
        return success(FOLDER_LIST, response);
    }

    @Transactional
    @PutMapping("/scrapList/editFolder")
    @ResponseStatus(OK)
    public Response editFolderName(@RequestParam("folderId")Long folderId, @RequestParam("folderName") String folderName){
        folderService.editFolderName(folderId, folderName);
        return success(EDIT_FOLDER_NAME);
    }

    @DeleteMapping("/scrapList/deledtFolder")
    @ResponseStatus(OK)
    public Response deletFolder(@RequestParam("folderId")Long folderId){
        folderService.deleteFolder(folderId);
        return success(DELETE_FOLDER);
    }

    @GetMapping("/scrapList/folderId")
    @ResponseStatus(OK)
    public Response scrapList(@RequestParam("folderId")Long folderId){
        List<ScrapResponseDto> response = folderService.scrapList(folderId);
        return success(SCRAP_LIST, response);
    }


}
