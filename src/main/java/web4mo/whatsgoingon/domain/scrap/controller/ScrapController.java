package web4mo.whatsgoingon.domain.scrap.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.domain.scrap.dto.FolderResponseDto;
import web4mo.whatsgoingon.domain.scrap.dto.ScrapPageDto;
import web4mo.whatsgoingon.domain.scrap.entity.Folder;
import web4mo.whatsgoingon.domain.scrap.service.ScrapService;
import web4mo.whatsgoingon.response.Response;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static web4mo.whatsgoingon.response.Message.*;
import static web4mo.whatsgoingon.response.Response.success;

@RestController
@RequiredArgsConstructor
@Tag(name = "Scrap Controller")
@RequestMapping(value="/scraping")
public class ScrapController {
    private final ScrapService scrapService;

    @GetMapping("/clickScrap")
    @ResponseStatus(OK) // 메인페이지에서 연필 눌렀을때
    public Response clickScrap(){
        List <FolderResponseDto> folderList = scrapService.clickScrap();
        return success(CLICK_LIST, folderList);
    }
    @PostMapping("/addScrap")
    @ResponseStatus(OK)
    @Transactional // 연필 눌러서 폴더 띄우고 폴더 선택해서 저장할때
    public Response scrapMain(@RequestParam(value = "articleId")Long articleId,
                              @RequestParam(value = "folderId")Long folderId){
        Long scrapId = scrapService.scrapMain(articleId, folderId);
        return success(SCRAP_MAIN, scrapId);
    }

    @GetMapping("/scrapPage")
    @ResponseStatus(OK) // 스크랩 페이지

    public Response scrapPage(@RequestParam(value = "scrapId")Long scrapId){
        ScrapPageDto scrapPageDto = scrapService.scrapPage(scrapId);
        return success(SCRAP_PAGE, scrapPageDto);
    }

    @DeleteMapping("/deleteScrap")
    @ResponseStatus(OK)
    public Response deleteScrap(@RequestParam(value = "scrapId")Long scrapId){
        scrapService.deleteScrap(scrapId);
        return success(DELETE_SCRAP);
    }

    @PutMapping("/scrapMemo")
    @ResponseStatus(OK)
    @Transactional
    public Response scrapMemo(@RequestParam(value = "scrapId")Long scrapId,
                              @RequestParam(value = "memo")String memo){
        scrapService.scrapMemo(scrapId, memo);
        return success(SCRAP_MEMO);
    }
    @PostMapping("/scrapAi")
    @ResponseStatus(OK)
    @Transactional
    public Response scrapAI(@RequestParam(value = "scrapId")Long scrapId){
        String response = scrapService.scrapAI(scrapId);
        return success(SCRAP_AI, response);
    }

}
