package com.codestates.favorite;

import com.codestates.member.entity.Member;
import com.codestates.room.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByRoomAndMember(Room room, Member member);
    Page<Favorite> findByMemberMemberId(Long memberId, Pageable pageable);
}
