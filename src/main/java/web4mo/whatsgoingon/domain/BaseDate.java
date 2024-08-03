package web4mo.whatsgoingon.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseDate {

    @CreatedDate
    private String createdAt;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
