package web4mo.whatsgoingon.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web4mo.whatsgoingon.domain.scrap.entity.Folder;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;

//@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByMember(Member member);
    List<Folder> findByMemberOrderByName(Member member);
    List<Folder> findByMemberOrderByModifiedAt(Member member);

    Folder findAById(Long folderId);
}
