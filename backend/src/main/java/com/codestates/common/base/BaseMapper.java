package com.codestates.common.base;

import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.tag.dto.TagDto;
import java.util.List;
import java.util.stream.Collectors;

public interface BaseMapper {
    default BaseDto.FillRoomResponseDtos fillRoomDatas_Member(Room room, Member member) {
        BaseDto.FillRoomResponseDtos responseDtos = new BaseDto.FillRoomResponseDtos();
        responseDtos.setRoomId(room.getRoomId());
        responseDtos.setTitle(room.getTitle());
        responseDtos.setInfo(room.getInfo());
        responseDtos.setMemberMaxCount(room.getMemberMaxCount());
        responseDtos.setImageUrl(room.getImageUrl());
        responseDtos.setPrivate(room.isPrivate());
        responseDtos.setPassword(room.getPassword());
        responseDtos.setFavoriteCount(room.getFavoriteCount());
        responseDtos.setFavorite(room.getFavoriteRoomList().stream().anyMatch(favorite -> favorite.getMember().equals(member) && favorite.isFavorite()));
        responseDtos.setTags(getRoomTags(room.getRoomTagList()));
        return responseDtos;
    }

    default List<TagDto.TagResponseDto> getRoomTags(List<RoomTag> roomTagList) {
        return roomTagList.stream()
                .map(memberRoom -> {
                    TagDto.TagResponseDto memberRoomTagDtos = new TagDto.TagResponseDto();
                    memberRoomTagDtos.setRoomId(memberRoom.getRoom().getRoomId());
                    memberRoomTagDtos.setTagId(memberRoom.getTag().getTagId());
                    memberRoomTagDtos.setName(memberRoom.getTag().getName());
                    return memberRoomTagDtos;
                })
                .collect(Collectors.toList());
    }

    default List<TagDto.TagResponseDto> getMemberTags(List<MemberTag> memberTagList) {
        return memberTagList.stream()
                .map(memberTag -> {
                    TagDto.TagResponseDto memberTagDtos = new TagDto.TagResponseDto();
                    memberTagDtos.setTagId(memberTag.getTag().getTagId());
                    memberTagDtos.setName(memberTag.getTag().getName());
                    return memberTagDtos;
                }).collect(Collectors.toList());
    }
}
