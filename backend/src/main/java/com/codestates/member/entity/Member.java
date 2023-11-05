package com.codestates.member.entity;

import com.codestates.common.entity.BaseEntity;
import com.codestates.common.history.RoomHistory;
import com.codestates.favorite.Favorite;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false ,unique = true)
    private String email;

    @Column
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

    @Column(name = "is_admin")
    private Boolean isAdmin=false;

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

    @OneToMany(mappedBy = "member")
    private List<RoomHistory> roomHistoryList = new ArrayList<>(); //history

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

    //리팩토링 1차
    @OneToMany(mappedBy = "member")
    private List<Favorite> favoriteList = new ArrayList<>();

    //리팩토링 1차
    @Builder
    public Member(Long memberId, String email, String password, String nickname, String imageUrl, int favoriteCount, int createdCount, int recordeCount, Boolean isAdmin, boolean isVoted, LocalDateTime deletionDate, MemberStatus status, List<MemberRoom> memberRoomList, List<MemberTag> memberTagList, List<RoomHistory> roomHistoryList, String provider, String providerId, List<String> roles, List<Favorite> favoriteList) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.favoriteCount = favoriteCount;
        this.createdCount = createdCount;
        this.recordeCount = recordeCount;
        this.isAdmin = isAdmin;
        this.isVoted = isVoted;
        this.deletionDate = deletionDate;
        this.status = MemberStatus.ACTIVE;
        this.memberRoomList = memberRoomList;
        this.memberTagList = memberTagList;
        this.roomHistoryList = roomHistoryList;
        this.provider = provider;
        this.providerId = providerId;
        this.roles = roles;
        this.favoriteList = favoriteList;
    }
}
