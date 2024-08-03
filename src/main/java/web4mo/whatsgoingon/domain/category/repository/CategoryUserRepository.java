package web4mo.whatsgoingon.domain.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web4mo.whatsgoingon.domain.category.entity.CategoryUser;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;

public interface CategoryUserRepository extends JpaRepository<CategoryUser,Long> {
    Boolean existsByMember(Member member);
    List<CategoryUser> findByMember(Member member);
    void deleteByMember(Member member);
}
