package com.codestates.member.entity;

import com.codestates.room.entity.Room;
import com.codestates.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name = "member_room")
public class MemberRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberRoomId;

    @Enumerated(value = EnumType.STRING)
    private Favorite favorite = Favorite.NONE;

    @Enumerated(value = EnumType.STRING)
    private History history;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    @Getter
    public enum Authority {
        ADMIN, USER
    }


    @Getter
    public enum Favorite {
        NONE , LIKE
    }


    @Getter
    public enum History {
        VISITED
    }
}
