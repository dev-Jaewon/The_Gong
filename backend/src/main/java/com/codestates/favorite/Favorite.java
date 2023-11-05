package com.codestates.favorite;

import com.codestates.member.entity.Member;
import com.codestates.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Favorite { //리팩토링 1차 (favorite 패키지 전체)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long favoriteId;

    @Column
    private boolean isFavorite;

    @ManyToOne @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne @JoinColumn(name = "ROOM_ID")
    private Room room;

    public boolean isFavorite() {
        return isFavorite;
    }
}


