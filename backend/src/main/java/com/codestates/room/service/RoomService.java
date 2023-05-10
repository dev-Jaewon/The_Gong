package com.codestates.room.service;


import com.codestates.member.entity.MemberRoom;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.repository.MemberRepository;
import com.codestates.member.repository.MemberRoomRepository;
import com.codestates.member.service.MemberService;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.room.repository.RoomRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {

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

    private final MemberService memberService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;

    public RoomService(MemberService memberService, RoomRepository roomRepository, MemberRepository memberRepository, MemberRoomRepository memberRoomRepository) {
        this.memberService = memberService;
        this.roomRepository = roomRepository;
        this.memberRepository = memberRepository;
        this.memberRoomRepository = memberRoomRepository;
    }


    public Room createRoom(Room room, long adminMemberId) {
        Member findMember = memberRepository.findById(adminMemberId).orElseThrow(()-> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        verifyExistsTitle(room.getTitle());

        room.setAdminNickname(findMember.getNickname());
        findMember.setCreatedCount(findMember.getCreatedCount()+1);

        memberRepository.save(findMember);
        roomRepository.save(room);

        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setMember(findMember);
        memberRoom.setRoom(room);
        memberRoom.setFavorite(MemberRoom.Favorite.NONE);
        memberRoom.setAuthority(MemberRoom.Authority.ADMIN);
        memberRoom.setHistory(MemberRoom.History.VISITED);
        memberRoomRepository.save(memberRoom);

        return room;
    }



    public Room updateRoom(Room room, long adminMemberId) {
        Room findRoom = findVerifiedRoom(room.getRoomId());
        Member findMember = memberService.findMember(adminMemberId);
        if(!findRoom.getAdminMemberId().equals(findMember.getMemberId())){
            throw new BusinessLogicException(ExceptionCode.ONLY_ADMIN);
        }

        Optional.ofNullable(room.getTitle())
                .ifPresent(title -> findRoom.setTitle(title));
        Optional.ofNullable(room.getInfo())
                .ifPresent(info -> findRoom.setInfo(info));
        Optional.ofNullable(room.getNotice())
                .ifPresent(notice->findRoom.setNotice(notice));
        Optional.ofNullable(room.getImageUrl())
                .ifPresent(image -> findRoom.setImageUrl(image));
        Optional.ofNullable(room.getMemberMaxCount())
                .ifPresent(max -> findRoom.setMemberMaxCount(max));

        Optional.ofNullable(room.isPrivate())
                        .ifPresent(is -> findRoom.setPrivate(is));

        if (room.isPrivate() && room.getPassword().isEmpty() || room.getPassword().equals(null)) {
                throw new BusinessLogicException(ExceptionCode.NEED_PASSWORD);

        } else if (!room.isPrivate() && !room.getPassword().isEmpty() || !room.getPassword().equals(null)) {
            findRoom.setPassword(null);

        } else {
            findRoom.setPassword(room.getPassword());
        }

        Optional.ofNullable(room.getRoomTagList())
                .ifPresent(tagList-> {
                    findRoom.getRoomTagList().clear();

                    for(RoomTag tag : tagList){
                        tag.setRoom(findRoom);
                        findRoom.getRoomTagList().add(tag);
                    }});

        roomRepository.save(findRoom);
        return findRoom;
    }



    public Room switchAdmin(Room room, long newAdminId) {
        Member findMember = memberRepository.findById(room.getAdminMemberId()).get();
        Member newAdminMember = memberRepository.findById(newAdminId).get();

        Room findRoom = findVerifiedRoom(room.getRoomId());
        findRoom.setAdminMemberId(newAdminMember.getMemberId());
        findRoom.setAdminNickname(newAdminMember.getNickname());


        //새로운방장회원의 권한은 ADMIN 으로 변경
        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setMember(newAdminMember);
        memberRoom.setRoom(findRoom);
        memberRoom.setAuthority(MemberRoom.Authority.ADMIN);
        memberRoomRepository.save(memberRoom);


        //기존방장회원의 권한은 User 로 변경
        findMember.getMemberRoomList().stream()
                .filter(r -> !r.getRoom().getRoomId().equals(findRoom.getRoomId()))
                        .findFirst()
                                .ifPresent(mr -> mr.setAuthority(MemberRoom.Authority.USER));
        memberRepository.save(findMember);
        roomRepository.save(findRoom);

        return findRoom;
    }



    public void deleteRoom(long roomId) {
        Room findRoom = findVerifiedRoom(roomId);
        roomRepository.delete(findRoom);
    }



    public void addFavorite(Room room, boolean isFavorite, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(()-> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Room findRoom = findVerifiedRoom(room.getRoomId());

        if(findMember.isVoted()) {
            throw new BusinessLogicException(ExceptionCode.DOUBLE_VOTE);

        } else if (isFavorite) {
            findMember.setFavoriteCount(findMember.getFavoriteCount() + 1);
            findRoom.setFavoriteCount(findRoom.getFavoriteCount() + 1);
            findMember.setVoted(true);

            Optional<MemberRoom> optionalMemberRoom = findRoom.getMemberRoomList()
                    .stream()
                    .filter(r -> r.getRoom().equals(findRoom))
                            .findFirst();

            if(optionalMemberRoom.isPresent()) { //수정
                MemberRoom memberRoom = optionalMemberRoom.get();
                memberRoom.setMember(findMember);
                memberRoom.setRoom(findRoom);
                memberRoom.setFavorite(MemberRoom.Favorite.LIKE);
                memberRoomRepository.save(memberRoom);
            }

            memberRepository.save(findMember);
            roomRepository.save(findRoom);
        }
    }



    public void undoFavorite(Room room, boolean isFavorite, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(()-> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Room findRoom = findVerifiedRoom(room.getRoomId());

        if(findMember.isVoted()) {
            throw new BusinessLogicException(ExceptionCode.PLEASE_VOTE);
        } else if (!isFavorite) {
            findMember.setFavoriteCount(findMember.getFavoriteCount() - 1);
            findRoom.setFavoriteCount(findRoom.getFavoriteCount() - 1);
            findMember.setVoted(false);

            Optional<MemberRoom> optionalMemberRoom = findRoom.getMemberRoomList()
                    .stream()
                    .filter(r -> r.getRoom().equals(findRoom))
                    .findFirst();

            MemberRoom memberRoom = memberRoomRepository.findByRoom(optionalMemberRoom.get().getRoom());
            memberRoom.setFavorite(MemberRoom.Favorite.NONE);

            memberRoomRepository.save(memberRoom);
            memberRepository.save(findMember);
            roomRepository.save(findRoom);
        }
    }



    public Page<MemberRoom> findRoomUsers(int page, int size, long roomId){
        Pageable pageable = PageRequest.of(page,size,Sort.by("roomId").descending());
        Room room = findVerifiedRoom(roomId);

        List<MemberRoom> memberList = room.getMemberRoomList()
                .stream()
                .filter(member -> !member.getRoom().getRoomId().equals(room.getRoomId()))
                .collect(Collectors.toList());

        if(memberList == null || memberList.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return new PageImpl<>(memberList, pageable, memberList.size());
    }



    public Page<Room> findNewRooms(int page, int size){
        Page<Room> roomPage = roomRepository.findAll(PageRequest.of(page,size,Sort.by("roomId").descending()));
        List<Room> roomList = roomPage.getContent();

        return new PageImpl<>(roomList, roomPage.getPageable(), roomPage.getTotalElements());
    }



    public Page<Room> findRecommendRooms(int page, int size){
        return null; //추천알고리즘
    }



    private Room findVerifiedRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        Room findRoom = room.orElseThrow(()->new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND));
        return findRoom;
    }



    public Page<Room> searchRoomsByKeyword(String keyword, Pageable pageable) {
        return roomRepository.findByTitleContainingIgnoreCaseOrRoomTagListContainingIgnoreCase(keyword,keyword,pageable);
    }



    private void verifyExistsTitle(String title) {
        Optional<Room> optionalRoom = roomRepository.findByTitle(title);
        if(optionalRoom.isPresent()){
            throw new BusinessLogicException(ExceptionCode.ROOM_EXIST);
        }
    }
}
