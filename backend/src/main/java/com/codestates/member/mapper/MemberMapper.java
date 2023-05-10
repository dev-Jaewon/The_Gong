package com.codestates.member.mapper;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.dto.MemberRoomTagDtos;
import com.codestates.member.dto.MemberTagDtos;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.RoomTag;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member postDtoToMember(MemberDto.Post requestBody);
    Member patchDtoToMember(MemberDto.Patch requestBody);




    //Todo : 회원생성 응답
    default MemberDto.PostResponseDto memberToPostResponseDto(Member createMember) {
        if (createMember == null) {
            return null;
        }
        MemberDto.PostResponseDto postResponseDto = new MemberDto.PostResponseDto();

        if (createMember.getMemberId() != null) {
            postResponseDto.setMemberId(createMember.getMemberId());
        }
        postResponseDto.setMemberId(createMember.getMemberId());
        postResponseDto.setEmail(createMember.getEmail());
        postResponseDto.setNickname(createMember.getNickname());
        postResponseDto.setFavoriteCount(createMember.getFavoriteCount());
        postResponseDto.setCreationCount(createMember.getCreatedCount());
        postResponseDto.setRecordRoomCount(createMember.getRecordeCount());
        postResponseDto.setCreatedAt(createMember.getCreatedAt());
        postResponseDto.setLastModifiedAt(createMember.getLastModifiedAt());
        return postResponseDto;
    }





    //Todo : 회원수정 응답
    default MemberDto.PatchResponseDto memberToPatchResponseDto(Member createMember) {
        if (createMember == null) {
            return null;
        }
        MemberDto.PatchResponseDto patchResponseDto = new MemberDto.PatchResponseDto();

        if (createMember.getMemberId() != null) {
            patchResponseDto.setMemberId(createMember.getMemberId());
        }
        patchResponseDto.setMemberId(createMember.getMemberId());
        patchResponseDto.setNickname(createMember.getNickname());
        patchResponseDto.setEmail(createMember.getEmail());
        patchResponseDto.setImageUrl(createMember.getImageUrl());
        patchResponseDto.setFavoriteCount(createMember.getFavoriteCount());
        patchResponseDto.setCreationCount(createMember.getCreatedCount());
        patchResponseDto.setRecordRoomCount(createMember.getRecordeCount());
        patchResponseDto.setCreatedAt(createMember.getCreatedAt());
        patchResponseDto.setLastModifiedAt(createMember.getLastModifiedAt());
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
                })
                .collect(Collectors.toList());
    }





    //Todo : 회원조회 응답
    default MemberDto.GetResponseDto memberToGetResponseDto(Member member) {
        if (member == null) {
            return null;
        }
        MemberDto.GetResponseDto getResponseDto = new MemberDto.GetResponseDto();

        if (member.getMemberId() != null) {
            getResponseDto.setMemberId(member.getMemberId());
        }
        getResponseDto.setMemberId(member.getMemberId());
        getResponseDto.setNickname(member.getNickname());
        getResponseDto.setEmail(member.getEmail());
        getResponseDto.setImageUrl(member.getImageUrl());
        getResponseDto.setFavoriteCount(member.getFavoriteCount());
        getResponseDto.setCreationCount(member.getCreatedCount());
        getResponseDto.setRecordRoomCount(member.getRecordeCount());
        getResponseDto.setCreatedAt(member.getCreatedAt());
        getResponseDto.setLastModifiedAt(member.getLastModifiedAt());
        getResponseDto.setTags(getMemberTags(member.getMemberTagList()));
        return getResponseDto;
    }





    //Todo : 찜한채팅방 목록
    default List<MemberDto.LikeRoomResponseDtos> memberToLikeResponseDtos(List<MemberRoom> memberRoomList, long memberId){

        return memberRoomList.stream()
                .filter(memberRoom -> memberRoom.getMember().getMemberId() == memberId && memberRoom.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .map(room -> {
                    MemberDto.LikeRoomResponseDtos responseDtos = new MemberDto.LikeRoomResponseDtos();
                    responseDtos.setRoomId(room.getMemberRoomId());
                    responseDtos.setTitle(room.getRoom().getTitle());
                    //인포빠져있음
                    responseDtos.setImageUrl(room.getRoom().getImageUrl());
                    responseDtos.setAdminNickname(room.getRoom().getAdminNickname());
                    responseDtos.setFavoriteCount(room.getRoom().getFavoriteCount());
                    responseDtos.setMemberMaxCount(room.getRoom().getMemberMaxCount());
                    responseDtos.setMemberCurrentCount(room.getRoom().getMemberCurrentCount());
                    responseDtos.setPrivate(room.getRoom().isPrivate());
                    responseDtos.setCreatedAt(room.getCreatedAt());
                    responseDtos.setLastModifiedAt(room.getLastModifiedAt()); //
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
                    //인포빠져있음
                    responseDtos.setImageUrl(room.getImageUrl());
                    responseDtos.setAdminNickname(room.getAdminNickname());
                    responseDtos.setFavoriteCount(room.getFavoriteCount());
                    responseDtos.setMemberMaxCount(room.getMemberMaxCount());
                    responseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
                    responseDtos.setPrivate(room.isPrivate());
                    responseDtos.setCreatedAt(room.getCreatedAt());
                    responseDtos.setLastModifiedAt(room.getLastModifiedAt()); //
                    responseDtos.setTags(getRoomTags(room.getRoomTagList()));
                    return responseDtos;
                })
                .collect(Collectors.toList());
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
                    //인포빠져있음
                    responseDtos.setImageUrl(room.getImageUrl());
                    responseDtos.setAdminNickname(room.getAdminNickname());
                    responseDtos.setFavoriteCount(room.getFavoriteCount());
                    responseDtos.setMemberMaxCount(room.getMemberMaxCount());
                    responseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
                    responseDtos.setPrivate(room.isPrivate());
                    responseDtos.setCreatedAt(room.getCreatedAt());
                    responseDtos.setLastModifiedAt(room.getLastModifiedAt()); //
                    responseDtos.setTags(getRoomTags(room.getRoomTagList()));
                    return responseDtos;
                })
                .collect(Collectors.toList());
    }






//    전체채팅방조회 사용 X
//    default List<MemberDto.GetResponseDtos> memberToGetResponseDtos(List<Member> memberList) {
//        return memberList.stream()
//                .map(member -> {
//                    MemberDto.GetResponseDtos getResponseDtos = new MemberDto.GetResponseDtos();
//                    getResponseDtos.setMemberId(member.getMemberId());
//                    getResponseDtos.setEmail(member.getEmail());
//                    getResponseDtos.setNickname(member.getNickname());
//                    getResponseDtos.setImageUrl(member.getImageUrl());
//                    getResponseDtos.setFavoriteCount(member.getFavoriteCount());
//                    getResponseDtos.setCreationCount(member.getCreatedCount());
//                    getResponseDtos.setRecordRoomCount(member.getRecordeCount());
//                    getResponseDtos.setCreatedAt(member.getCreatedAt());
//                    getResponseDtos.setLastModifiedAt(member.getLastModifiedAt());
//                    getResponseDtos.setTags(getMemberTags(member.getMemberTagList()));
//                    return getResponseDtos;
//                })
//                .collect(Collectors.toList());
//    }

}

