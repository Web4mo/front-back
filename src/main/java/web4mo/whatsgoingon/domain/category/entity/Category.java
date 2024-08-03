package web4mo.whatsgoingon.domain.category.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Category {
    //정치/경제/사회/ 생활 문화/ IT과학/ 세계/스포츠/연예
    POLITICS("정치", List.of("국회", "북한", "국방","행정")),
    ECONOMY("경제",List.of("금융","산업","부동산","글로벌 경제","생활 경제")),
    SOCIETY("사회",List.of("사건","교육","언론","환경","인권","식품")),
    LIFESTYLE_CULTURE("생활문화",List.of("건강정보","도로","여행","음식","패션","공연")),
    IT_SCIENCE("IT과학",List.of("모바일","인터넷","통신","보안","컴퓨터","게임")),
    WORLD("세계",List.of("아시아","미국","유럽","중동")),
    SPORTS("스포츠",List.of("야구","해외야구","축구","해외축구","농구","배구")),
    ENTERTAINMENT("연예",List.of("방송","드라마","뮤직","해외 연예"));

    private final String name;
    private final List<String> topics;


    Category(String name, List<String> topics) {
        this.name=name;
        this.topics=new ArrayList<>(topics);
    }

    //한글 카테고리 -> Category로 변경
    public static Category valueofCategory(String name){
        for(Category label: values()){
            if(label.getName().equals(name)){
                return label;
            }
        }
        throw new IllegalStateException("없는 category: " + name);
    }
    //키워드로 카테고리 찾기
    public static Category getCategory(String value){
        for(Category label: values()){
            for(String key: label.getTopics()){
                if(key.equals(value)){
                    return label;
                }
            }
        }
        throw new IllegalStateException("속하는 category 없음: " + value);
    }
    //키워드 존재하는지 확인
    public static Boolean isKeyword(String value){
        for(Category label: values()){
            for(String key: label.getTopics()){
                if(key.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }



}
