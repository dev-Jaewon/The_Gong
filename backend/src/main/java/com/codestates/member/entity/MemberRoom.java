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
@Table(name = "member_room")
public class MemberRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberRoomId;

    @Enumerated(value = EnumType.STRING)
    private Favorite favorite;

    @Enumerated(value = EnumType.STRING)
    private History history;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
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
