package web4mo.whatsgoingon.domain.mypage.dto;

import lombok.*;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CalendarDto {
    private Long id;
    private String loginId;
    private LocalDate attendDate;
}
