package com.codestates.room.service;

import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import com.codestates.member.repository.MemberRoomRepository;
import com.codestates.member.service.MemberService;
import com.codestates.room.entity.RoomTag;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoomService {

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
        Member findMember = memberRepository.findById(adminMemberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        verifyExistsTitle(room.getTitle());

        room.setAdminNickname(findMember.getNickname());
        findMember.setCreatedCount(findMember.getCreatedCount() + 1);

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
        if (!findRoom.getAdminMemberId().equals(findMember.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ONLY_ADMIN);
        }
        Optional.ofNullable(room.getTitle())
                .ifPresent(title -> findRoom.setTitle(title));
        Optional.ofNullable(room.getInfo())
                .ifPresent(info -> findRoom.setInfo(info));
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
                .ifPresent(tagList -> {
                    findRoom.getRoomTagList().clear();

                    for (RoomTag tag : tagList) {
                        tag.setRoom(findRoom);
                        findRoom.getRoomTagList().add(tag);
                    }
                });

        roomRepository.save(findRoom);
        return findRoom;
    }



    public Room switchAdmin(Room room, long newAdminId) {
        Member newAdminMember = memberRepository.findById(newAdminId).get();

        Room findRoom = findVerifiedRoom(room.getRoomId());
        MemberRoom memberRoom = memberRoomRepository.findByRoom(findRoom);
        memberRoom.setMember(newAdminMember);
        memberRoom.setRoom(findRoom);
        memberRoom.setAuthority(MemberRoom.Authority.ADMIN);
        memberRoomRepository.save(memberRoom);
        return memberRoom.getRoom();
    }



    public void deleteRoom(long roomId) {
        Room findRoom = findVerifiedRoom(roomId);
        roomRepository.delete(findRoom);
    }



    public void addFavorite(Room room, boolean isFavorite, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Room findRoom = findVerifiedRoom(room.getRoomId());

        if (findMember.isVoted()) {
            throw new BusinessLogicException(ExceptionCode.DOUBLE_VOTE);

        } else if (isFavorite) {
            findMember.setFavoriteCount(findMember.getFavoriteCount() + 1);
            findRoom.setFavoriteCount(findRoom.getFavoriteCount() + 1);
            findMember.setVoted(true);

            Optional<MemberRoom> optionalMemberRoom = findRoom.getMemberRoomList()
                    .stream()
                    .filter(r -> r.getRoom().equals(findRoom))
                    .findFirst();

            if (optionalMemberRoom.isPresent()) { //수정
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
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Room findRoom = findVerifiedRoom(room.getRoomId());

        if (!findMember.isVoted()) {
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


    public Page<MemberRoom> findRoomUsers(int page, int size, long roomId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("roomId").descending());
        Room room = findVerifiedRoom(roomId);

        List<MemberRoom> memberList = room.getMemberRoomList()
                .stream()
                .filter(member -> !member.getRoom().getRoomId().equals(room.getRoomId()))
                .collect(Collectors.toList());

        if (memberList == null || memberList.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return new PageImpl<>(memberList, pageable, memberList.size());
    }



    public Page<Room> findNewRooms(int page, int size) {
        Page<Room> roomPage = roomRepository.findAll(PageRequest.of(page, size, Sort.by("roomId").descending()));
        List<Room> roomList = roomPage.getContent();

        return new PageImpl<>(roomList, roomPage.getPageable(), roomPage.getTotalElements());
    }




    //Todo : (미회원) 태그포함 + 찜많은순/생성순 정렬
    public Page<Room> findUnauthorizedRooms(int page, int size, String sort) {
        Page<Room> roomPage;
        List<Room> roomList;
        if (!sort.isEmpty() && !sort.isEmpty() && sort.equals("createdAt")) {
            roomPage = roomRepository.findAll(PageRequest.of(page, size, getSortingMethod(sort).descending()));
        } else if (!sort.isEmpty() && !sort.isEmpty() && sort.equals("favoriteCount")) {
            roomPage = roomRepository.findAll(PageRequest.of(page, size, getSortingMethod(sort).descending()));
        } else {
            throw new BusinessLogicException(ExceptionCode.SORT_DOSE_NOT_EXIST);
        }
        roomList = roomPage.getContent();
        return new PageImpl<>(roomList, roomPage.getPageable(), roomPage.getTotalElements());
    }



    //Todo : (회원) 태그포함 + 찜많은순/생성순 정렬
    public Page<Room> findRecommendRooms(int page, int size, String sort, long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        List<MemberTag> memberTags = findMember.getMemberTagList();
        Page<Room> roomPage;
        List<Room> roomList;

        if (!sort.isEmpty() && !sort.isEmpty() && sort.equals("createdAt")) {
            roomPage = roomRepository.findAll(PageRequest.of(page, size, getSortingMethod(sort).descending()));
        } else if (!sort.isEmpty() && !sort.isEmpty() && sort.equals("favoriteCount")) {
            roomPage = roomRepository.findAll(PageRequest.of(page, size, getSortingMethod(sort).descending()));
        } else {
            throw new BusinessLogicException(ExceptionCode.SORT_DOSE_NOT_EXIST);
        }

        roomList = roomPage.getContent();
        List<Room> recommendList = roomList.stream().filter(room -> room.getRoomTagList().stream()
                .anyMatch(memberTags::contains)).collect(Collectors.toList());

        return new PageImpl<>(recommendList, roomPage.getPageable(), recommendList.size());
    }


    public Room findVerifiedRoom(Long roomId) {
        Optional<Room> room = roomRepository.findById(roomId);
        Room findRoom = room.orElseThrow(() -> new BusinessLogicException(ExceptionCode.ROOM_NOT_FOUND));
        return findRoom;
    }


    private void verifyExistsTitle(String title) {
        Optional<Room> optionalRoom = roomRepository.findByTitle(title);
        if (optionalRoom.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ROOM_EXIST);
        }
    }


    private Sort getSortingMethod(String sort) {
        switch (sort) {
            case "createdAt":
                return Sort.by("createdAt").descending();
            case "favoriteCount":
                return Sort.by("favoriteCount").descending();
            default:
                return null;
        }
    }


    public Page<Room> searchRoomsByKeyword(String keyword, Pageable pageable) {
        return roomRepository.findByTitleContainingIgnoreCaseOrRoomTagListContainingIgnoreCase(keyword, keyword, pageable);
    }
}
