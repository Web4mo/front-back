package web4mo.whatsgoingon.domain.mypage.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import web4mo.whatsgoingon.domain.mypage.dto.CalendarDto;
import web4mo.whatsgoingon.domain.mypage.service.CalendarService;
import web4mo.whatsgoingon.response.Response;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static web4mo.whatsgoingon.response.Message.*;
import static web4mo.whatsgoingon.response.Response.success;

@Tag(name = "Calendar Controller")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    @GetMapping
    @ResponseStatus(OK)
    public Response getAttendance() {
        List<CalendarDto> attendanceList = calendarService.getAttendance();
        return success(FETCH_ATTENDANCE, attendanceList);
    }

    @PostMapping
    @ResponseStatus(OK)
    public Response attend() {
        CalendarDto attend = calendarService.attend();
        return success(ATTEND, attend);
    }
}
