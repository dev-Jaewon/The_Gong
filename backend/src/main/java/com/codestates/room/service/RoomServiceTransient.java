package com.codestates.room.service;

import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.repository.MemberRoomRepository;
import com.codestates.member.service.MemberService;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceTransient {

    private final RoomRepository roomRepository;
    private final MemberService memberService;
    private final MemberRoomRepository memberRoomRepository;

    public void enterChatRoom(Long roomId, String nickname) {
        /*
         * 1. 방, 멤버 엔티티 찾기
         * 2. 새로운 멤버 룸 생성
         * 3. 현재 인원 1증가
         * 4. 현재 연관관계 메서드가 없으나, 룸리스트에 새로운 멤버룸 추가
         * */
        // 1
        Room foundRoom = roomRepository.findById(roomId).get();
        Member foundMember = memberService.findVerifiedMember(nickname);
        log.info("Room {} is founded", foundRoom.getRoomId());

        // 2
        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setMember(foundMember);
        memberRoom.setRoom(foundRoom);
        memberRoom.setFavorite(MemberRoom.Favorite.NONE);
        memberRoom.setAuthority(MemberRoom.Authority.USER);
        memberRoom.setHistory(MemberRoom.History.VISITED);
        MemberRoom madeMemberRoom = memberRoomRepository.save(memberRoom);
        log.info("Member {} is entering the room. . . ", madeMemberRoom.getMember().getMemberId());

        // 3
        foundRoom.setMemberCurrentCount(foundRoom.getMemberCurrentCount() + 1);
        log.info("Current Count is {} of {}", foundRoom.getMemberCurrentCount(), foundRoom.getMemberMaxCount());

        // 4
        foundRoom.getMemberRoomList().add(madeMemberRoom);
        log.info("New member {} is in Room {}", madeMemberRoom.getMember().getMemberId(), roomId);

//        return foundRoom.getMemberRoomList().stream().map(a -> {
//            Member member = memberService.findVerifiedMember(a.getMember().getMemberId());
//            return member.getNickname();
//        }).collect(Collectors.toList());
    }

    public void leaveSession(String roomId) {
        /*
         *
         * */
        Long longRoomId = Long.parseLong(roomId);
        Room foundRoom = roomRepository.findById(longRoomId).get();
        log.info("A member leave {} room", foundRoom.getRoomId());


        foundRoom.setMemberCurrentCount(foundRoom.getMemberCurrentCount() -1);
        log.info("Current count of this room is  {} of {}", foundRoom.getMemberCurrentCount(), foundRoom.getMemberMaxCount());

//        return foundRoom.getMemberRoomList().stream().map(a -> {
//            Member member = memberService.findVerifiedMember(a.getMember().getMemberId());
//            return member.getNickname();
//        }).collect(Collectors.toList());
    }
}
