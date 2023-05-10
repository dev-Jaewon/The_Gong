package com.codestates.member.entity;

import com.codestates.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String imageUrl;

    @Column
    private int favoriteCount;

    @Column
    private int createdCount;

    @Column
    private int recordeCount;

    @Column
    private boolean isVoted;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @OneToMany(mappedBy = "member")
    private List<MemberRoom> memberRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTag> memberTagList = new ArrayList<>();

    public enum MemberStatus {
        ACTIVE,
        DELETE
    }
}
