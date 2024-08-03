package web4mo.whatsgoingon.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Member,Long> {
    boolean existsByLoginId(String loginId);
    Optional<Member> findByLoginId(String loginId);
    List<Member> findAllBy();
}
