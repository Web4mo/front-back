package web4mo.whatsgoingon.domain.category.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;
import web4mo.whatsgoingon.domain.user.entity.Member;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;


    public void updateCategory(Category category){
        this.category=category;
    }
}
