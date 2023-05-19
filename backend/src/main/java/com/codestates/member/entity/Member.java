package com.codestates.member.entity;

import com.codestates.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
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

    @Column
    private LocalDateTime deletionDate;

    @Enumerated(value = EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVE;

    @OneToMany(mappedBy = "member")
    private List<MemberRoom> memberRoomList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberTag> memberTagList = new ArrayList<>();

    private String provider;
    private String providerId;

    public enum MemberStatus {
        ACTIVE,
        DELETE
    }

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public void assignRoles(List<String> roles) {
        this.roles = roles;
    }
}
