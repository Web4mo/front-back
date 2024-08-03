package web4mo.whatsgoingon.domain.mypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web4mo.whatsgoingon.domain.mypage.entity.Attendance;

import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByMemberId(Long memberId);
}
