package com.codestates.room.repository;

import com.codestates.room.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByTitle(String title);
    List<Room> findAllByInfoContaining(String query, Pageable pageable);

    List<Room> findAllByRoomTagList_TagNameContaining(String query, Pageable pageable);

    List<Room> findAllByTitleContaining(String query, Pageable pageable);
}
