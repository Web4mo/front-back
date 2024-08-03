package web4mo.whatsgoingon.response;

public class Message {
    // UserController
    public static String SIGN_UP = "회원가입에 성공했습니다.";
    public static String LOG_IN = "로그인에 성공했습니다.";
    public static String LOG_OUT = "로그아웃에 성공했습니다.";
    public static String REISSUE = "토큰 재발급에 성공했습니다.";

    //UserCategoryController
    public static String SAVE_CATEGORY = "카테고리 키워드 저장 성공했습니다.";
    public static String SAVE_MEDIA ="언론사 저장 성공했습니다.";

    // ArticleController
    public static String CONTENT_MAIN = "기사 전문 불러오기 성공했습니다.";
    public static String GET_MAIN = "기사 리스트 불러오기 성공했습니다.";


    // FolderController
    public static String FOLDER_LIST = "폴더 목록 불러오기 성공했습니다";
    public static String ADD_FOLDER = "폴더 추가 성공했습니다.";
    public static String EDIT_FOLDER_NAME = "폴더 명 수정 성공했습니다.";
    public static String DELETE_FOLDER = "폴더 삭제 성공했습니다.";
    public static String SCRAP_LIST = "스크랩 목록 불러오기 성공했습니다.";

    // ScrapController
    public static String CLICK_LIST = "폴더 목록 불러오기 성공했습니다.";
    public static String SCRAPING = "스크랩 성공했습니다.";
    public static String SCRAP_MAIN = "메인페이지에서 스크랩 저장 성공했습니다.";
    public static String SCRAP_PAGE = "스크랩 페이지 띄우기 성공했습니다.";
    public static String DELETE_SCRAP = "스크랩 삭제 성공했습니다.";
    public static String SCRAP_MEMO = "스크랩 메모 수정 성공했습니다.";
    public static String SCRAP_AI = "AI 요약 성공했습니다.";
    public static String CLICK_SCRAP = "연필모양 눌렀을때 폴더 리스트 반환 성공했습니다.";

    // ProfileController
    public static String FETCH_PROFILE = "프로필 불러오기 성공했습니다.";
    public static String UPDATE_PROFILE = "프로필 수정 성공했습니다.";
    public static String EDIT_PASSWORD = "비밀번호 변경 성공했습니다.";
    public static String UPLOAD_IMG = "프로필 사진 업로드 성공했습니다.";
    public static String DELETE_IMG = "프로필 사진 삭제 성공했습니다.";

    // CalendarController
    public static String FETCH_ATTENDANCE = "출석 기록 불러오기 성공했습니다.";
    public static String ATTEND = "출석체크 성공했습니다.";

}
