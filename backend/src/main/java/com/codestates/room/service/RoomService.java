package com.codestates.room.service;

import com.codestates.member.entity.MemberRoom;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

    private final RoomRepository roomRepository;

    public void enterMember(Long roomId) {
        /*
        * 1. 방을 찾고
        * 2. 새로운 멤버 룸 생성
        * 3. 현재 인원 1증가
        * 4. 현재 연관관계 메서드가 없으나, 룸리스트에 새로운 멤버룸 추가
        * */
        // 1
        Room foundRoom = roomRepository.findById(roomId).get();
        log.info("Room {} is founded", foundRoom.getRoomId());
        // 2
        MemberRoom madeMemberRoom = new MemberRoom();
        log.info("Member {} is entering the room. . . ", madeMemberRoom.getMember().getMemberId());
        // 3
        foundRoom.setMemberCurrentCount(foundRoom.getMemberCurrentCount() + 1);
        log.info("Current Count is {} of {}", foundRoom.getMemberCurrentCount(), foundRoom.getMemberMaxCount());
        // 4
        foundRoom.getMemberRoomList().add(madeMemberRoom);
        log.info("New member {} is in Room {}", madeMemberRoom.getMember().getMemberId(), roomId);
    }

    public void leaveSession(String roomId) {
        Long longRoomId = Long.parseLong(roomId);
        Room foundRoom = roomRepository.findById(longRoomId).get();
        log.info("A member leave {} room", foundRoom.getRoomId());


        foundRoom.setMemberCurrentCount(foundRoom.getMemberCurrentCount() -1);
        log.info("Current count of this room is  {} of {}", foundRoom.getMemberCurrentCount(), foundRoom.getMemberMaxCount());
    }

    /*
    * 여기 이하 테스트를 위해서 임시 생성함
    *
    * */
    public Room createChatRoom(String roomName, String roomPwd, boolean secretChk, int maxUsercnt) {

        Room room = new Room();
        room.setTitle(roomName);
        room.setPassword(roomPwd);
        room.setPrivate(secretChk);
        room.setMemberMaxCount(maxUsercnt);
        Room save = roomRepository.save(room);


        return save;
    }

    public List<Room> getALlRooms() {
        return roomRepository.findAll();
    }

    public Room getRoom(String roomId) {
        Long longRoomId = Long.parseLong(roomId);
        return roomRepository.findById(longRoomId).get();
    }
}
