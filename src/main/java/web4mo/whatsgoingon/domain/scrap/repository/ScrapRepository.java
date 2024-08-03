package web4mo.whatsgoingon.domain.scrap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import web4mo.whatsgoingon.domain.scrap.entity.Scrap;

import java.util.List;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByFolderId(Long folderId);
}
