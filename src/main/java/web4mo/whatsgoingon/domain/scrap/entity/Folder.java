package web4mo.whatsgoingon.domain.scrap.entity;

import jakarta.persistence.*;
import lombok.*;
import web4mo.whatsgoingon.domain.BaseTime;
import web4mo.whatsgoingon.domain.user.entity.Member;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Folder extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "folder_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Column
    private String name;

    public void updateName(String folderName) {
        this.name = folderName;
    }
}
