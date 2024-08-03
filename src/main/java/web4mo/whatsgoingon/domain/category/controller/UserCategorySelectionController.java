package web4mo.whatsgoingon.domain.category.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.domain.category.dto.MediaSelectionDto;
import web4mo.whatsgoingon.domain.category.dto.UserCategorySelectionDto;
import web4mo.whatsgoingon.domain.category.entity.Category;
import web4mo.whatsgoingon.domain.category.entity.Media;
import web4mo.whatsgoingon.domain.category.service.CategoryKeywordService;
import web4mo.whatsgoingon.domain.user.service.UserService;
import web4mo.whatsgoingon.response.Response;

import java.util.List;

import static web4mo.whatsgoingon.response.Message.SAVE_CATEGORY;
import static web4mo.whatsgoingon.response.Message.SAVE_MEDIA;
import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "User Category Selection Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
public class UserCategorySelectionController {
    private final CategoryKeywordService categoryKeywordService;
    private final UserService userService;

    @PostMapping("/SaveCategoryKeywords")
    public Response save_category(@RequestBody UserCategorySelectionDto userCategorySelectionDto){
        List<List<String>> userCategoryKeyword=categoryKeywordService.saveCategorykeyword(userCategorySelectionDto);
        return success(SAVE_CATEGORY,userCategoryKeyword);
    }

    @PostMapping("/SaveMedia")
    public Response save_media(@RequestBody MediaSelectionDto mediaSelectionDto){
        List<Media> mediaList=categoryKeywordService.saveMedia(mediaSelectionDto);
        if(mediaList==null)
            return success(SAVE_MEDIA,"선택한 미디어가 없습니다.");
        return success(SAVE_MEDIA,mediaList);
    }

    @GetMapping("/getCategory")
    public Response getcategory(){
        List<String> keywords= categoryKeywordService.userKeywords(userService.getCurrentMember());
        int randomIndex = (int) (Math.random() * keywords.size());
        String randomElement = keywords.get(randomIndex);
        Category category=categoryKeywordService.getCategoryfromkeyword(randomElement);
        return success(randomElement+"-"+category.getName());
    }

}
