package web4mo.whatsgoingon.domain.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web4mo.whatsgoingon.domain.category.dto.MediaSelectionDto;
import web4mo.whatsgoingon.domain.category.dto.UserCategorySelectionDto;
import web4mo.whatsgoingon.domain.category.entity.*;
import web4mo.whatsgoingon.domain.category.repository.CategoryUserRepository;
import web4mo.whatsgoingon.domain.category.repository.MediaRepository;
import web4mo.whatsgoingon.domain.category.repository.MediaUserRepository;
import web4mo.whatsgoingon.domain.category.repository.KeywordUserRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.repository.UserRepository;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryKeywordService {
    private final KeywordUserRepository keywordUserRepository;
    private final UserRepository userRepository;
    private final MediaUserRepository mediaUserRepository;
    private final MediaRepository mediaRepository;
    private final CategoryUserRepository categoryUserRepository;

    //카테고리 키워드 저장 및 수정
    @Transactional
    public List<List<String>> saveCategorykeyword(UserCategorySelectionDto userCategorySelectionDto){

        String userId = userCategorySelectionDto.getUserId();
        Optional<Member> optionalMember = userRepository.findByLoginId(userId);
        if(optionalMember.isEmpty()){
            throw new IllegalStateException("회원이 아닙니다.");
        }
        Member user= optionalMember.get();
        if(userCategorySelectionDto.getCategory().isEmpty() ||userCategorySelectionDto.getKeyword().isEmpty()){
            throw new IllegalStateException("선택한 카테고리나 키워드가 없습니다.");
        }
        if(categoryUserRepository.existsByMember(user)){
            categoryUserRepository.deleteByMember(user);
        }
        if(keywordUserRepository.existsByMember(user)){
            keywordUserRepository.deleteByMember(user);
        }
        for(String usercategory: userCategorySelectionDto.getCategory()){
            Category category=Category.valueofCategory(usercategory);
            CategoryUser categoryUser=CategoryUser.builder().category(category).member(user).build();
            categoryUserRepository.save(categoryUser);
        }
        for(String keyword: userCategorySelectionDto.getKeyword()){
            KeywordUser keywordUser = web4mo.whatsgoingon.domain.category.entity.KeywordUser.builder().keyword(keyword).member(user).build();
            keywordUserRepository.save(keywordUser);

        }

        List<String> categories= new ArrayList<>();
        for(CategoryUser categoryUser:categoryUserRepository.findByMember(user)){
            categories.add(categoryUser.getCategory().getName());
        }

        List<List<String>> categoryKeywords = new ArrayList<>();
        categoryKeywords.add(categories);
        categoryKeywords.add(userKeywords(user));
        return categoryKeywords;
    }

    //키워드에서 해당 카테고리 가져오기
    public Category getCategoryfromkeyword(String keyword){
        return Category.getCategory(keyword);
    }

    //유저가 선택한 키워드만 가져오기
    public List <String> userKeywords(Member member){
        List<String> keywords = new ArrayList<>();
        for(KeywordUser keyword: keywordUserRepository.findByMember(member)) {
            keywords.add(keyword.getKeyword());
        }
        return keywords;
    }

    //유저가 선택한 카테고리만 가져오기
    public List<Category> userCategories(Member member){
        List<Category> categories=new ArrayList<>();
        for(CategoryUser category: categoryUserRepository.findByMember(member)) {
            categories.add(category.getCategory());
        }
        return categories;
    }

    //유저가 선택한 카테고리 개수 가져오기
    public int userCategoryCount(Member member) {
        Set<Category> uniqueCategories = new HashSet<>();
        for (CategoryUser categoryUser : categoryUserRepository.findByMember(member)) {
            uniqueCategories.add(categoryUser.getCategory());
        }
        return uniqueCategories.size();
    }

    //유저가 선택한 언론사만 가져오기
    public List<String> userMedium(Member member){
        List<String> medium=new ArrayList<>();
        for(MediaUser media: mediaUserRepository.findByMember(member)) {
            medium.add(media.getMedia().getName());
        }
        return medium;
    }

    //언론사 저장하기
    @Transactional
    public List<Media> saveMedia(MediaSelectionDto mediaSelectionDto){
        if(mediaSelectionDto.getMedias().isEmpty()){
            return null;
        }
        String userId = mediaSelectionDto.getUserId();

        Optional<Member> optionalMember = userRepository.findByLoginId(userId);
        if(optionalMember.isEmpty()){
            throw new IllegalStateException("회원이 아닙니다.");
        }
        Member user= optionalMember.get();

        if(mediaUserRepository.existsByMember(user)){
            mediaUserRepository.deleteByMember(user);
        }
        if(mediaSelectionDto.getMedias()==null){
            return null;
        }
        List<Media> mediaList=new ArrayList<>();
        for(Map.Entry<String,String> entry: mediaSelectionDto.getMedias().entrySet() ){
            Media media;
            if(!mediaRepository.existsByName(entry.getKey())){
                media=Media.builder().name(entry.getKey()).link(entry.getValue()).build();
                mediaRepository.save(media);
                mediaList.add(mediaRepository.findByName(entry.getKey()));
            }
            else{
                continue;
            }
            MediaUser mediaUser=MediaUser.builder().media(media).member(user).build();
            mediaUserRepository.save(mediaUser);
        }
        return mediaList;
    }

    //
    //회원 정보 수정
    //
    //카테고리 변경
    public void updateUserCategories(Member member, List<String> interests) {
        log.info("변경전 "+userCategories(member).toString());
        categoryUserRepository.deleteByMember(member);
        for(String usercategory: interests){
            Category category=Category.valueofCategory(usercategory);
            CategoryUser categoryUser=CategoryUser.builder().category(category).member(member).build();
            categoryUserRepository.save(categoryUser);
        }
        log.info("수정 후 "+userCategories(member).toString());
    }

    //키워드 변경
    public void updateUserKeywords(Member member, List<String> keywords) {
        log.info("변경전 "+userKeywords(member).toString());
        keywordUserRepository.deleteByMember(member);
        for(String keyword: keywords){
            KeywordUser keywordUser = web4mo.whatsgoingon.domain.category.entity.KeywordUser.builder().keyword(keyword).member(member).build();
            keywordUserRepository.save(keywordUser);
        }
        log.info("수정 후 "+userKeywords(member).toString());

    }

    //언론사 변경
    public void updateUserMedium(Member member, Map<String, String> media) {
        log.info("변경전 "+userMedium(member).toString());
        mediaUserRepository.deleteByMember(member);
        if(!media.isEmpty()){
            List<Media> mediaList=new ArrayList<>();
            for(Map.Entry<String,String> entry: media.entrySet() ) {
                Media me;
                if (!mediaRepository.existsByName(entry.getKey())) {
                    me = Media.builder().name(entry.getKey()).link(entry.getValue()).build();
                    mediaRepository.save(me);
                    mediaList.add(mediaRepository.findByName(entry.getKey()));
                } else {
                    continue;
                }
                MediaUser mediaUser = MediaUser.builder().media(me).member(member).build();
                mediaUserRepository.save(mediaUser);
            }
        }
        log.info("수정 후 "+userMedium(member).toString());
    }
}
