package web4mo.whatsgoingon.domain.mypage.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.mypage.dto.CalendarDto;
import web4mo.whatsgoingon.domain.mypage.entity.Attendance;
import web4mo.whatsgoingon.domain.mypage.repository.CalendarRepository;
import web4mo.whatsgoingon.domain.user.entity.Member;
import web4mo.whatsgoingon.domain.user.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalendarService {
    private final UserService userService;
    private final CalendarRepository calendarRepository;


    public List<CalendarDto> getAttendance(){
        Member member = userService.getCurrentMember();
        List<Attendance> attendances = calendarRepository.findByMemberId(member.getId());
        return attendances.stream()
                .map(attendance -> CalendarDto.builder()
                        .id(attendance.getId())
                        .loginId(attendance.getMember().getLoginId())
                        .attendDate(attendance.getAttendAt())
                        .build())
                .collect(Collectors.toList());
    }

    public CalendarDto attend(){
        Member member = userService.getCurrentMember();
        LocalDate today = LocalDate.now();

        if (calendarRepository.findByMemberId(member.getId()).stream()
                .anyMatch(attendance -> attendance.getAttendAt().equals(today))) {
            throw new IllegalStateException("이미 오늘 출석했습니다.");
        }

        Attendance attendance = Attendance.builder()
                .member(member)
                .attendAt(LocalDate.from(today))
                .build();

        Attendance savedAttendance = calendarRepository.save(attendance);

        return CalendarDto.builder()
                .id(savedAttendance.getId())
                .loginId(savedAttendance.getMember().getLoginId())
                .attendDate(LocalDate.from(savedAttendance.getAttendAt()))
                .build();
    }
}
