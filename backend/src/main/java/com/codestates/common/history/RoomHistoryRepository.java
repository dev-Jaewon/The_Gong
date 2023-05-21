package com.codestates.common.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomHistoryRepository extends JpaRepository<RoomHistory, Long> {
    RoomHistory findByMember_MemberIdAndRoom_Title(long memberId, String roomTitle);
    List<RoomHistory> findAll();
}
