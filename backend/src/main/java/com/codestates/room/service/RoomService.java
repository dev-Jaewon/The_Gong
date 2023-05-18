package com.codestates.room.service;

import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import com.codestates.tag.entity.Tag;
import com.codestates.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomService {

    private final MemberService memberService;
    private final TagRepository tagRepository;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;


    public Room createRoom(Room room, long adminMemberId) {
        Member findMember = memberRepository.findById(adminMemberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        verifyExistsTitle(room.getTitle());

        room.setAdminNickname(findMember.getNickname());
        findMember.setCreatedCount(findMember.getCreatedCount() + 1);

        List<RoomTag> roomTagList = room.getRoomTagList().stream()
                .map(rt -> {
                    String roomTagName = rt.getTag().getName();
                    Optional<Tag> optionalTag = tagRepository.findByName(roomTagName);

                    if(optionalTag.isPresent()) rt.setTag(optionalTag.get());
                    else throw new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND);
                    return rt;
                }).collect(Collectors.toList());

        room.setRoomTagList(roomTagList);

        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setMember(findMember);
        memberRoom.setRoom(room);
        memberRoom.setFavorite(MemberRoom.Favorite.NONE);
        memberRoom.setAuthority(MemberRoom.Authority.ADMIN);
        memberRoom.setHistory(MemberRoom.History.VISITED);

        roomRepository.save(room);
        memberRoomRepository.save(memberRoom);
        memberRepository.save(findMember);

        return room;
    }


    public Room updateRoom(Room room, long adminMemberId) {
        Room findRoom = findVerifiedRoom(room.getRoomId());
        Member findMember = memberService.findMember(adminMemberId);

        if (!findRoom.getAdminMemberId().equals(findMember.getMemberId()))
            throw new BusinessLogicException(ExceptionCode.ONLY_ADMIN);

        ;
        //이미지 수정 제거
        Optional.ofNullable(room.getTitle())
                .ifPresent(findRoom::setTitle);
        Optional.ofNullable(room.getInfo())
                .ifPresent(findRoom::setInfo);
        Optional.ofNullable(room.getMemberMaxCount())
                .ifPresent(findRoom::setMemberMaxCount);
        Optional.ofNullable(room.isPrivate())
                .ifPresent(findRoom::setPrivate);


        if (!room.isPrivate() && room.getPassword() != null && !room.getPassword().isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_PASSWORD_REQUIRED);
        }

        if (room.isPrivate()) {
            Optional.ofNullable(room.getPassword())
                    .ifPresent(password -> {
                        if (password != null && !password.isEmpty()) {
                            findRoom.setPassword(password);
                        } else {
                            throw new BusinessLogicException(ExceptionCode.NEED_PASSWORD);
                        }
                    });
        } else {
            findRoom.setPassword(null);
        }


        if (room.getRoomTagList() != null) {
            findRoom.getRoomTagList().clear();

            for (RoomTag tag : room.getRoomTagList()) {
                tag.setRoom(findRoom);
            }
            findRoom.setRoomTagList(room.getRoomTagList());
        }

//        if (room.isPrivate()) {
//            Optional.ofNullable(room.getPassword())
//                    .ifPresent(password -> {
//                        if (password != null && !password.isEmpty()) {
//                            findRoom.setPassword(password);
//
//                        } else if(password.isEmpty()){
//                            throw new BusinessLogicException(ExceptionCode.NEED_PASSWORD);
//
//                        } else {
//                                findRoom.setPassword(null);
//                        }});
//        }

        roomRepository.save(findRoom);
        return findRoom;
    }



    public Room switchAdmin(Room room, long newAdminId) {
        Member newAdminMember = memberRepository.findById(newAdminId).get();

        Room findRoom = findVerifiedRoom(room.getRoomId());
        findRoom.setAdminMemberId(newAdminMember.getMemberId());
        findRoom.setAdminNickname(newAdminMember.getNickname());
        findRoom.setImageUrl(newAdminMember.getImageUrl());
        roomRepository.save(findRoom);

        MemberRoom memberRoom = memberRoomRepository.findByRoom(findRoom);
        memberRoom.setMember(newAdminMember);
        memberRoom.setRoom(findRoom);
        memberRoom.setAuthority(MemberRoom.Authority.ADMIN);
        memberRoomRepository.save(memberRoom);
        return memberRoom.getRoom();
    }



    public void addFavorite(Room room, boolean isFavorite, long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        Room findRoom = findVerifiedRoom(room.getRoomId());

        Optional<MemberRoom> existingFavorite = findMember.getMemberRoomList().stream()
                .filter(mr -> mr.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .filter(mr -> mr.getRoom().getRoomId() == findRoom.getRoomId())
                .findFirst();

        if(existingFavorite.isPresent())
            throw new BusinessLogicException(ExceptionCode.DOUBLE_VOTE);

        else if (isFavorite) {
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

        Optional<MemberRoom> existingFavorite = findMember.getMemberRoomList().stream()
                .filter(mr -> mr.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .filter(mr -> mr.getRoom().getRoomId() == findRoom.getRoomId())
                .findFirst();

        if (!existingFavorite.isPresent()) {
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


    public Page<Room> findNewRooms(int page, int size) {
        Page<Room> roomPage = roomRepository.findAll(PageRequest.of(page, size, Sort.by("roomId").descending()));
        List<Room> roomList = roomPage.getContent();

        return new PageImpl<>(roomList, roomPage.getPageable(), roomPage.getTotalElements());
    }


    // 외래키 제약조건 해결
    public void deleteRoom(long roomId) {
        Room findRoom = findVerifiedRoom(roomId);
        List<MemberRoom> memberRoomList = findRoom.getMemberRoomList();
        for (MemberRoom memberRoom : memberRoomList) {
            memberRoomRepository.delete(memberRoom);
        }
        roomRepository.delete(findRoom);
    }




    //Todo : DB 체크 메서드
    public Room findVerifiedRoom(long roomId) {
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





    //Todo : (미회원) 태그포함 + 찜많은순/생성순 정렬
    public Page<Room> findUnauthorizedRooms(int page, int size, String sort) {
        Page<Room> roomPage;
        List<Room> roomList;
        return null;
        //return new PageImpl<>(roomList, roomPage.getPageable(), roomPage.getTotalElements());
    }



    //Todo : (회원) 태그포함 + 찜많은순/생성순 정렬
    public Page<Room> findRecommendRooms(int page, int size, String sort, long memberId) {
        Member findMember = memberService.findVerifiedMember(memberId);
        List<MemberTag> memberTags = findMember.getMemberTagList();
        Page<Room> roomPage;
        List<Room> roomList;
        return null;
        // List<Room> recommendList = roomList.stream().filter(room -> room.getRoomTagList().stream()
        //        .anyMatch(memberTags::contains)).collect(Collectors.toList());
        // return new PageImpl<>(recommendList, roomPage.getPageable(), recommendList.size());
    }



}
