package com.codestates.member.entity;

import com.codestates.room.entity.Room;
import com.codestates.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberRoomId;


    @Enumerated(value = EnumType.STRING)
    private Favorite favorite = Favorite.NONE;


    @Enumerated(value = EnumType.STRING)
    private History history = History.FIRST;


    @Enumerated(value = EnumType.STRING)
    private Authority authority;


    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private Room room;


    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;


    public enum Favorite {
        NONE , LIKE
    }


    public enum History {
        FIRST , VISITED
    }


    public enum Authority {
        ADMIN , USER
    }
}