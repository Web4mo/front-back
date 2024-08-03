package web4mo.whatsgoingon.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web4mo.whatsgoingon.domain.category.entity.Media;
import web4mo.whatsgoingon.domain.category.entity.MediaUser;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;

@Repository
public interface MediaUserRepository extends JpaRepository<MediaUser, Long> {
    Boolean existsByMember(Member member);
    void deleteByMember(Member member);
    List<MediaUser> findByMember(Member member);


}



