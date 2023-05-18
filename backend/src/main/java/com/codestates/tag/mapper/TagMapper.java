package com.codestates.tag.mapper;

import com.codestates.member.entity.MemberRoom;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.tag.dto.TagDto;
import com.codestates.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TagMapper {



    //Todo : 태그조회 (태그 클릭시, 해당태그를 사용한 스터디룸 목록조회)
    default List<TagDto.GetRoomTagResponseDto> tagToRoomTagResponseDto(List<Room> roomTagList) {
        if (roomTagList == null) return null;
        List<TagDto.GetRoomTagResponseDto> list = new ArrayList<TagDto.GetRoomTagResponseDto>(roomTagList.size());

        for (Room room : roomTagList) {
            list.add(getRoomContainsTag(room));
        }
        return list;
    }

    default TagDto.GetRoomTagResponseDto getRoomContainsTag(Room room) {
        if (room == null) return null;

        TagDto.GetRoomTagResponseDto responseDto = new TagDto.GetRoomTagResponseDto();
        responseDto.setRoomId(room.getRoomId());
        responseDto.setTitle(room.getTitle());
        responseDto.setInfo(room.getInfo());
        responseDto.setImageUrl(room.getImageUrl());
        responseDto.setPrivate(room.isPrivate());
        responseDto.setFavoriteCount(room.getFavoriteCount());
        responseDto.setFavoriteStatus(getRoomFavorite(room, room.getMemberRoomList()));
        responseDto.setMemberMaxCount(room.getMemberMaxCount());
        responseDto.setTags(getRoomTags(room.getRoomTagList()));

        return responseDto;
    }

    default MemberRoom.Favorite getRoomFavorite(Room room, List<MemberRoom> memberRoomList) {
        if (memberRoomList == null) return null;
        MemberRoom memberRoom = memberRoomList.stream()
                .filter(r -> r.getRoom().getRoomId().equals(room.getRoomId())).findFirst().get();

        return memberRoom.getFavorite();
    }

    default List<TagDto.TagResponseDto> getRoomTags(List<RoomTag> roomTagList) {

        return roomTagList.stream()
                .map(memberRoom -> {
                    TagDto.TagResponseDto tagResponseDto = new TagDto.TagResponseDto();
                    tagResponseDto.setTagId(memberRoom.getTag().getTagId());
                    tagResponseDto.setName(memberRoom.getTag().getName());
                    return tagResponseDto;
                })
                .collect(Collectors.toList());
    }



    List<TagDto.TagResponseDto> tagToGetTagResponseDtos(List<Tag> tagList);

}

