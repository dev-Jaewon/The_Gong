package com.codestates.tag.mapper;

import com.codestates.common.base.BaseDto;
import com.codestates.common.base.BaseMapper;
import com.codestates.member.entity.Member;
import com.codestates.room.entity.Room;
import com.codestates.tag.dto.TagDto;
import com.codestates.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper extends BaseMapper { //리팩토링 1차

    //Todo : 태그조회 (해당태그를 사용한 스터디룸 목록조회)
    default List<BaseDto.FillRoomResponseDtos> tagToRoomTagResponseDto(List<Room> roomTagList) {
        if (roomTagList == null) return null;
        List<BaseDto.FillRoomResponseDtos> list = new ArrayList<BaseDto.FillRoomResponseDtos>(roomTagList.size());
        for (Room room : roomTagList)
            list.add(fillRoomDatas_Member(room, new Member())); //쓸모없는 Member 객체 생성 이부분 개선필요함!
        return list;
    }

    List<TagDto.TagResponseDto> tagToGetTagResponseDtos(List<Tag> tagList);
}