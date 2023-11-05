package com.codestates.room.mapper;

import com.codestates.member.entity.Member;
import com.codestates.room.dto.RoomDto;
import com.codestates.tag.dto.TagDto;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.tag.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {


    //Todo : 방생성
    default Room postDtoToRoom(RoomDto.Post requestBody) {
        if (requestBody == null) return null;

        Room room = new Room();
        room.setAdminMemberId(requestBody.getAdminMemberId());
        room.setTitle(requestBody.getTitle());
        room.setInfo(requestBody.getInfo());
        room.setImageUrl(requestBody.getImageUrl());
        room.setPassword(requestBody.getPassword());
        room.setPrivate(requestBody.isPrivate());
        room.setMemberMaxCount(requestBody.getMemberMaxCount());

        List<RoomTag> roomTags = requestBody.getTags().stream()
                .map(st -> {
                    RoomTag roomTag = new RoomTag();
                    Tag tag = new Tag();
                    tag.setName(st);
                    roomTag.setTag(tag);
                    roomTag.setRoom(room);
                    return roomTag;
                }).collect(Collectors.toList());
        room.setRoomTagList(roomTags);
        return room;
    }


    //Todo : 방생성 응답
    default RoomDto.PostResponseDto roomToPostResponseDto(Room createRoom) {
        if (createRoom == null) return null;
        RoomDto.PostResponseDto postResponseDto = new RoomDto.PostResponseDto();

        if (createRoom.getRoomId() != null) postResponseDto.setRoomId(createRoom.getRoomId());
        postResponseDto.setTitle(createRoom.getTitle());
        postResponseDto.setInfo(createRoom.getInfo());

        if (createRoom.getAdminMemberId() != null)
            postResponseDto.setAdminMemberId(createRoom.getAdminMemberId());

        postResponseDto.setImageUrl(createRoom.getImageUrl());
        postResponseDto.setMemberMaxCount(createRoom.getMemberMaxCount());
        postResponseDto.setMemberCurrentCount(createRoom.getMemberCurrentCount());
        postResponseDto.setPrivate(createRoom.isPrivate());
        postResponseDto.setPassword(createRoom.getPassword());
        postResponseDto.setFavoriteCount(createRoom.getFavoriteCount());
        postResponseDto.setTags(getRoomTags(createRoom.getRoomTagList()));

        return postResponseDto;
    }


    //Todo : 방수정

    default Room patchDtoToRoom(RoomDto.Patch requestBody) {
        if (requestBody == null) {
            return null;
        }

        Room room = new Room();

        room.setRoomId(requestBody.getRoomId());
        room.setAdminMemberId(requestBody.getAdminMemberId());
        room.setTitle(requestBody.getTitle());
        room.setInfo(requestBody.getInfo());

        room.setPassword(requestBody.getPassword());
        //System.out.println(requestBody.getPassword()); // 비밀번호 가져오기

        room.setPrivate(requestBody.isPrivate());
        room.setMemberMaxCount(requestBody.getMemberMaxCount());
        room.setRoomTagList(patchRoomTags(requestBody.getTags()));
        return room;
    }

    default List<RoomTag> patchRoomTags(List<TagDto.TagResponseDto> tags) {
        if (tags == null) return new ArrayList<>();
        List<RoomTag> list = new ArrayList<>(tags.size());
        for (TagDto.TagResponseDto roomTagDto : tags)
            list.add(roomTagDtoToRoomTag(roomTagDto));
        return list;
    }

    default RoomTag roomTagDtoToRoomTag(TagDto.TagResponseDto tag) {
        if (tag == null) return null;
        RoomTag roomTag = new RoomTag();
        Tag tag1 = new Tag();
        tag1.setTagId(tag.getTagId());
        tag1.setName(tag.getName());
        roomTag.setTag(tag1);
        return roomTag;
    }


    //Todo : 방수정 응답
    default RoomDto.PatchResponseDto roomToPatchResponseDto(Room room, Member member) {
        if (room == null) return null;
        RoomDto.PatchResponseDto patchResponseDto = new RoomDto.PatchResponseDto();

        if (room.getRoomId() != null) patchResponseDto.setRoomId(room.getRoomId());
        patchResponseDto.setTitle(room.getTitle());
        patchResponseDto.setInfo(room.getInfo());
        patchResponseDto.setAdminNickname(room.getAdminNickname());
        patchResponseDto.setImageUrl(room.getImageUrl());
        patchResponseDto.setMemberMaxCount(room.getMemberMaxCount());
        patchResponseDto.setMemberCurrentCount(room.getMemberCurrentCount());
        patchResponseDto.setPrivate(room.isPrivate());
        patchResponseDto.setPassword(room.getPassword());
        patchResponseDto.setFavoriteCount(room.getFavoriteCount());
        patchResponseDto.setFavorite((room.getFavoriteRoomList().stream().anyMatch(favorite -> favorite.getMember().equals(member) && favorite.isFavorite())));
        patchResponseDto.setTags(getRoomTags(room.getRoomTagList()));

        return patchResponseDto;
    }


    //Todo : 방장권한 위임 & 응답
    default Room patchAdminDtoToRoom(RoomDto.PatchAdmin requestBody) {
        if (requestBody == null) return null;
        Room room = new Room();
        room.setAdminMemberId(requestBody.getNewAdminId());
        room.setRoomId(requestBody.getRoomId());
        return room;
    }

    default RoomDto.PatchAdminResponseDto roomToPatchAdminResponseDto(Room room) {
        if (room == null) return null;

        RoomDto.PatchAdminResponseDto patchAdminResponseDto = new RoomDto.PatchAdminResponseDto();

        if (room.getRoomId() != null) patchAdminResponseDto.setRoomId(room.getRoomId());
        patchAdminResponseDto.setRoomId(room.getRoomId());
        patchAdminResponseDto.setAdminMemberId(room.getAdminMemberId());
        patchAdminResponseDto.setAdminNickname(room.getAdminNickname());
        patchAdminResponseDto.setImageUrl(room.getImageUrl());
        return patchAdminResponseDto;
    }


    //Todo : 최신순 방목록조회 응답
    default List<RoomDto.GetRoomResponseDtos> roomToNewRoomResponseDtos(List<Room> roomList) {
        if (roomList == null) return null;
        List<RoomDto.GetRoomResponseDtos> list = new ArrayList<RoomDto.GetRoomResponseDtos>(roomList.size());

        for (Room room : roomList)
            list.add(roomToGetNewRoomResponseDtos(room));
        return list;
    }

    default RoomDto.GetRoomResponseDtos roomToGetNewRoomResponseDtos(Room room) {
        if (room == null) return null;
        RoomDto.GetRoomResponseDtos getNewRoomResponseDtos = new RoomDto.GetRoomResponseDtos();

        if (room.getRoomId() != null) getNewRoomResponseDtos.setRoomId(room.getRoomId());
        getNewRoomResponseDtos.setTitle(room.getTitle());
        getNewRoomResponseDtos.setInfo(room.getInfo());
        getNewRoomResponseDtos.setImageUrl(room.getImageUrl());
        getNewRoomResponseDtos.setMemberMaxCount(room.getMemberMaxCount());
        getNewRoomResponseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
        getNewRoomResponseDtos.setPrivate(room.isPrivate());
        getNewRoomResponseDtos.setPassword(room.getPassword());
        getNewRoomResponseDtos.setFavoriteCount(room.getFavoriteCount());
        getNewRoomResponseDtos.setFavorite(room.getFavoriteRoomList().isEmpty()); // 최근생성한 룸....... 처음 오는 사람은 조항요가 안보이는데 회원은 보여야 한다.....
        getNewRoomResponseDtos.setTags(getRoomTags(room.getRoomTagList()));

        return getNewRoomResponseDtos;
    }

    default List<TagDto.TagResponseDto> getRoomTags(List<RoomTag> roomTagList) {
        return roomTagList.stream()
                .map(roomTag -> new TagDto.TagResponseDto(roomTag.getTag().getTagId(), roomTag.getTag().getName()))
                .collect(Collectors.toList());
    }


    //Todo : 추천스터디방 조회 (비회원)
    default List<RoomDto.GetRoomResponseDtos> memberToNonMemberRecommendResponseDtos(List<Room> recommendList) {
        if (recommendList == null) return null;
        List<RoomDto.GetRoomResponseDtos> list = new ArrayList<RoomDto.GetRoomResponseDtos>(recommendList.size());

        for (Room room : recommendList)
            list.add(roomToGetNonMemberResponseDtos(room));
        return list;
    }

    default RoomDto.GetRoomResponseDtos roomToGetNonMemberResponseDtos(Room room) {
        if (room == null) return null;
        RoomDto.GetRoomResponseDtos recommendRoomResponseDtos = new RoomDto.GetRoomResponseDtos();

        if (room.getRoomId() != null) recommendRoomResponseDtos.setRoomId(room.getRoomId());
        recommendRoomResponseDtos.setTitle(room.getTitle());
        recommendRoomResponseDtos.setInfo(room.getInfo());
        recommendRoomResponseDtos.setImageUrl(room.getImageUrl());
        recommendRoomResponseDtos.setMemberMaxCount(room.getMemberMaxCount());
        recommendRoomResponseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
        recommendRoomResponseDtos.setPrivate(room.isPrivate());
        recommendRoomResponseDtos.setPassword(room.getPassword());
        recommendRoomResponseDtos.setFavoriteCount(room.getFavoriteCount());
        recommendRoomResponseDtos.setTags(getRoomTags(room.getRoomTagList()));
        return recommendRoomResponseDtos;
    }


    //Todo : 추천스터디방 조회 (회원)
    default List<RoomDto.GetRoomResponseDtos> memberToRecommendResponseDtos(List<Room> recommendList, Member member) {
        if (recommendList == null) return null;
        List<RoomDto.GetRoomResponseDtos> list = new ArrayList<RoomDto.GetRoomResponseDtos>(recommendList.size());

        for (Room room : recommendList)
            list.add(roomToGetRecommendResponseDtos(room, member));
        return list;
    }


    default RoomDto.GetRoomResponseDtos roomToGetRecommendResponseDtos(Room room, Member member) {
        if (room == null) return null;
        RoomDto.GetRoomResponseDtos recommendRoomResponseDtos = new RoomDto.GetRoomResponseDtos();
        if (room.getRoomId() != null) recommendRoomResponseDtos.setRoomId(room.getRoomId());
        recommendRoomResponseDtos.setTitle(room.getTitle());
        recommendRoomResponseDtos.setInfo(room.getInfo());
        recommendRoomResponseDtos.setImageUrl(room.getImageUrl());
        recommendRoomResponseDtos.setMemberMaxCount(room.getMemberMaxCount());
        recommendRoomResponseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
        recommendRoomResponseDtos.setPrivate(room.isPrivate());
        recommendRoomResponseDtos.setPassword(room.getPassword());
        recommendRoomResponseDtos.setFavoriteCount(room.getFavoriteCount());
        recommendRoomResponseDtos.setFavorite(room.getFavoriteRoomList().stream().anyMatch(favorite -> favorite.getMember().equals(member) && favorite.isFavorite()));
        recommendRoomResponseDtos.setTags(getRoomTags(room.getRoomTagList()));
        return recommendRoomResponseDtos;
    }
}
