package com.codestates.room.repository;

import com.codestates.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findByTitle(String title);
    Page<Room> findByTitleContainingIgnoreCaseOrRoomTagListContainingIgnoreCase(String keyword, String tag, Pageable pageable);
}
