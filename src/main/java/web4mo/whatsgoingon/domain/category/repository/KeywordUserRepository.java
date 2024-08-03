package web4mo.whatsgoingon.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web4mo.whatsgoingon.domain.category.entity.KeywordUser;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;

@Repository
public interface KeywordUserRepository extends JpaRepository<KeywordUser, Long> {
    Boolean existsByMember(Member member);
    List<KeywordUser> findByMember(Member member);
    void deleteByMember(Member member);
}
