package com.codestates.member.mapper;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.dto.MemberRoomTagDtos;
import com.codestates.member.dto.MemberTagDtos;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.tag.dto.TagDto;
import com.codestates.tag.entity.Tag;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member postDtoToMember(MemberDto.Post requestBody);
    Member patchNicknameDtoToMember(MemberDto.PatchNickname requestBody);
    Member patchImageDtoToMember(MemberDto.PatchImage requestBody);


    default MemberDto.PatchImageResponseDto memberToPatchImageResponseDto(Member responseMember){
        if(responseMember == null) return null;
        MemberDto.PatchImageResponseDto responseDto = new MemberDto.PatchImageResponseDto();
        responseDto.setMemberId(responseMember.getMemberId());
        responseDto.setImageUrl(responseMember.getImageUrl());

        return  responseDto;
    }




    //Todo : 회원수정 응답 : 응답물어보기
    default MemberDto.PatchResponseDto memberToPatchResponseDto(Member createMember) {
        if (createMember == null) return null;
        MemberDto.PatchResponseDto patchResponseDto = new MemberDto.PatchResponseDto();

        if (createMember.getMemberId() != null) patchResponseDto.setMemberId(createMember.getMemberId());
        patchResponseDto.setMemberId(createMember.getMemberId());
        patchResponseDto.setNickname(createMember.getNickname());
        patchResponseDto.setEmail(createMember.getEmail());
        patchResponseDto.setImageUrl(createMember.getImageUrl());
        patchResponseDto.setTags(getMemberTags(createMember.getMemberTagList()));

        return patchResponseDto;
    }

    default List<MemberTagDtos> getMemberTags(List<MemberTag> memberTagList) {

        return memberTagList.stream()
                .map(memberTag -> {
                    MemberTagDtos memberTagDtos = new MemberTagDtos();
                    memberTagDtos.setTagId(memberTag.getTag().getTagId());
                    memberTagDtos.setName(memberTag.getTag().getName());
                    return memberTagDtos;

                }).collect(Collectors.toList());
    }





    //Todo : 찜한채팅방 목록
    default List<MemberDto.LikeRoomResponseDtos> memberToLikeResponseDtos(List<MemberRoom> memberRoomList, long memberId){

        return memberRoomList.stream()
                .filter(memberRoom -> memberRoom.getMember().getMemberId() == memberId && memberRoom.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .map(room -> {
                    MemberDto.LikeRoomResponseDtos responseDtos = new MemberDto.LikeRoomResponseDtos();
                    responseDtos.setRoomId(room.getMemberRoomId());
                    responseDtos.setTitle(room.getRoom().getTitle());
                    responseDtos.setInfo(room.getRoom().getInfo());
                    responseDtos.setImageUrl(room.getRoom().getImageUrl());
                    responseDtos.setFavoriteCount(room.getRoom().getFavoriteCount());
                    responseDtos.setMemberMaxCount(room.getRoom().getMemberMaxCount());
                    //responseDtos.setMemberCurrentCount(room.getRoom().getMemberCurrentCount());
                    responseDtos.setPrivate(room.getRoom().isPrivate());
                    responseDtos.setFavoriteStatus(room.getFavorite());
                    responseDtos.setTags(getRoomTags(room.getRoom().getRoomTagList()));
                    return responseDtos;
                })
                .collect(Collectors.toList());
    }

    default List<MemberRoomTagDtos> getRoomTags(List<RoomTag> roomTagList) {
        return roomTagList.stream()
                .map(memberRoom -> {
                    MemberRoomTagDtos memberRoomTagDtos = new MemberRoomTagDtos();
                    memberRoomTagDtos.setRoomId(memberRoom.getRoom().getRoomId());
                    memberRoomTagDtos.setTagId(memberRoom.getTag().getTagId());
                    memberRoomTagDtos.setName(memberRoom.getTag().getName());
                    return memberRoomTagDtos;
                })
                .collect(Collectors.toList());
    }





    //Todo : 생성한채팅방 목록
    default List<MemberDto.CreatedRoomResponseDtos> memberToCreatedResponseDtos(List<MemberRoom> memberRoomList){
        return memberRoomList.stream()
                .filter(memberRoom -> memberRoom.getAuthority().equals(MemberRoom.Authority.ADMIN))
                .map(MemberRoom::getRoom)
                .map(room -> {
                    MemberDto.CreatedRoomResponseDtos responseDtos = new MemberDto.CreatedRoomResponseDtos();
                    responseDtos.setRoomId(room.getRoomId());
                    responseDtos.setTitle(room.getTitle());
                    responseDtos.setInfo(room.getInfo());
                    responseDtos.setImageUrl(room.getImageUrl());
                    responseDtos.setFavoriteCount(room.getFavoriteCount());
                    responseDtos.setFavoriteStatus(getRoomFavorite(room,room.getMemberRoomList()));
                    responseDtos.setMemberMaxCount(room.getMemberMaxCount());
                    //responseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
                    responseDtos.setPrivate(room.isPrivate());
                    responseDtos.setTags(getRoomTags(room.getRoomTagList()));
                    return responseDtos;
                })
                .collect(Collectors.toList());
    }

    default MemberRoom.Favorite getRoomFavorite(Room room, List<MemberRoom> memberRoomList){
        if (memberRoomList == null) {
            return null;
        }
        MemberRoom memberRoom = memberRoomList.stream().filter(r -> r.getRoom().getRoomId().equals(room.getRoomId())).findFirst().get();
        return memberRoom.getFavorite();
    }





    //Todo : 채팅방 방문기록 목록
    default List<MemberDto.RecordRoomResponseDtos> memberToRecordResponseDtos(List<MemberRoom> memberRoomList){
        return memberRoomList.stream()
                .filter(memberRoom -> memberRoom.getHistory().equals(MemberRoom.History.VISITED))
                .map(MemberRoom::getRoom)
                .map(room -> {
                    MemberDto.RecordRoomResponseDtos responseDtos = new MemberDto.RecordRoomResponseDtos();
                    responseDtos.setRoomId(room.getRoomId());
                    responseDtos.setTitle(room.getTitle());
                    responseDtos.setInfo(room.getInfo());
                    responseDtos.setImageUrl(room.getImageUrl());
                    responseDtos.setFavoriteCount(room.getFavoriteCount());
                    responseDtos.setMemberMaxCount(room.getMemberMaxCount());
                    responseDtos.setPrivate(room.isPrivate());
                    responseDtos.setTags(getRoomTags(room.getRoomTagList()));
                    return responseDtos;
                })
                .collect(Collectors.toList());
    }
}

