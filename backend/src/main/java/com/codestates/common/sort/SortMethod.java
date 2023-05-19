package com.codestates.common.sort;

import com.codestates.room.dto.RoomDto;
import com.codestates.room.entity.Room;
import lombok.Getter;

import java.util.Comparator;

@Getter
public class SortMethod {
    public static Comparator<Room> sortByFavoriteCount(){
        return (r1,r2) -> Integer.compare(r2.getFavoriteCount(), r1.getFavoriteCount());
    }

    public static Comparator<Room> sortByOld(){
        return Comparator.comparing(Room::getCreatedAt);
    }

    public static Comparator<Room> sortByNew(){
        return Comparator.comparing(Room::getCreatedAt).reversed();
    }
}
