package web4mo.whatsgoingon.domain.article.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.config.NaverApi.ArticleApiDto;
import web4mo.whatsgoingon.domain.article.dto.ArticleDto;
import web4mo.whatsgoingon.domain.article.entity.Article;
import web4mo.whatsgoingon.domain.article.service.ArticleService;
//import web4mo.whatsgoingon.domain.category.repository.UserCategoryKeywordRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.repository.UserRepository;
import web4mo.whatsgoingon.domain.user.service.UserService;
import web4mo.whatsgoingon.response.Response;
//import web4mo.whatsgoingon.domain.category.repository.UserCategoryKeywordRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static web4mo.whatsgoingon.response.Message.*;
import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "Article Controller", description = "기사 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping(value = "/article")
public class ArticleController {

    //private final UserCategoryKeywordRepository userCategoryKeywordRepository;
    private final ArticleService articleService;
    private final UserService userService;

    // Member member = userService.getCurrentMember();
    //RequestParam - keyword, page, sort 불필요?
    //멤버 정보 가져온 후 대입

    @Operation(summary = "기사를 가져옵니다.")
    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public Response getArticles(@RequestParam(required = false) String keyword,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(required = false) String sort) {
        try {
            List<ArticleDto> articles = articleService.getArticles(keyword, page, sort);

            return success(GET_MAIN, articles);
        } catch (Exception e) {
            log.error("기사 가져오기에 실패했습니다.", e);
            return Response.failure(HttpStatus.INTERNAL_SERVER_ERROR, "기사 가져오기에 실패했습니다.");
        }
    }
}