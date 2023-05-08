package com.codestates.member.mapper;

import com.codestates.member.dto.MemberDto;
import com.codestates.member.dto.MemberRoomTagDtos;
import com.codestates.member.dto.MemberTagDtos;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.RoomTag;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member postDtoToMember(MemberDto.Post requestBody);
    Member patchDtoToMember(MemberDto.Patch requestBody);





    //Todo : 회원생성 응답
    default MemberDto.PostResponseDto memberToPostResponseDto(Member createMember){
        if ( createMember == null ) {
            return null;
        }
        MemberDto.PostResponseDto postResponseDto = new MemberDto.PostResponseDto();

        if ( createMember.getMemberId() != null ) {
            postResponseDto.setMemberId( createMember.getMemberId() );
        }
        postResponseDto.setMemberId( createMember.getMemberId() );
        postResponseDto.setEmail( createMember.getEmail() );
        postResponseDto.setNickname( createMember.getNickname() );
        postResponseDto.setFavoriteCount( createMember.getFavoriteCount() );
        postResponseDto.setCreationCount( createMember.getCreatedCount() );
        postResponseDto.setRecordRoomCount( createMember.getRecordeCount() );
        postResponseDto.setCreatedAt( createMember.getCreatedAt() );
        postResponseDto.setLastModifiedAt( createMember.getLastModifiedAt() );
        return postResponseDto;
    }





    //Todo : 회원수정 응답
    default MemberDto.PatchResponseDto memberToPatchResponseDto(Member createMember){
        if ( createMember == null ) {
            return null;
        }
        MemberDto.PatchResponseDto patchResponseDto = new MemberDto.PatchResponseDto();

        if ( createMember.getMemberId() != null ) {
            patchResponseDto.setMemberId( createMember.getMemberId() );
        }
        patchResponseDto.setMemberId( createMember.getMemberId() );
        patchResponseDto.setNickname( createMember.getNickname() );
        patchResponseDto.setEmail( createMember.getEmail() );
        patchResponseDto.setImageUrl( createMember.getImageUrl() );
        patchResponseDto.setFavoriteCount( createMember.getFavoriteCount() );
        patchResponseDto.setCreationCount( createMember.getCreatedCount() );
        patchResponseDto.setRecordRoomCount( createMember.getRecordeCount() );
        patchResponseDto.setCreatedAt( createMember.getCreatedAt() );
        patchResponseDto.setLastModifiedAt( createMember.getLastModifiedAt() );
        patchResponseDto.setTags( getMemberTags(createMember.getMemberTagList()) );

        return patchResponseDto;
    }

    default List<MemberTagDtos> getMemberTags(List<MemberTag> memberTagList){
        if(memberTagList == null) {
            return null;
        }
        List<MemberTagDtos> responseDtoList = new ArrayList<>(memberTagList.size());
        for( MemberTag memberTag : memberTagList ) {
            responseDtoList.add ( fillMemberTags( memberTag ) );
        }
        return responseDtoList;
    }

    default MemberTagDtos fillMemberTags(MemberTag memberTag){
        if( memberTag == null) {
            return null;
        }
        MemberTagDtos responseDto = new MemberTagDtos();
        responseDto.setTagId(memberTag.getMemberTagId());
        responseDto.setName(memberTag.getTag().getName());
        return responseDto;
    }





    //Todo : 회원조회 응답
    default MemberDto.GetResponseDto memberToGetResponseDto(Member member){
        if ( member == null ) {
            return null;
        }

        MemberDto.GetResponseDto getResponseDto = new MemberDto.GetResponseDto();

        if ( member.getMemberId() != null ) {
            getResponseDto.setMemberId( member.getMemberId() );
        }
        getResponseDto.setMemberId( member.getMemberId() );
        getResponseDto.setNickname( member.getNickname() );
        getResponseDto.setEmail( member.getEmail() );
        getResponseDto.setImageUrl( member.getImageUrl() );
        getResponseDto.setFavoriteCount( member.getFavoriteCount() );
        getResponseDto.setCreationCount( member.getCreatedCount() );
        getResponseDto.setRecordRoomCount( member.getRecordeCount() );
        getResponseDto.setCreatedAt( member.getCreatedAt() );
        getResponseDto.setLastModifiedAt( member.getLastModifiedAt() );
        getResponseDto.setTags( getMemberTags( member.getMemberTagList() ) );
        return getResponseDto;
    }

    List<MemberDto.GetResponseDtos> memberToGetResponseDtos(List<Member> memberList);





    //Todo : 찜한채팅방 목록
    default List<MemberDto.LikeRoomResponseDtos> memberToLikeResponseDtos(List<MemberRoom> memberRoomList){
        if ( memberRoomList == null ) {
            return null;
        }
        List<MemberDto.LikeRoomResponseDtos> list = new ArrayList<MemberDto.LikeRoomResponseDtos>( memberRoomList.size() );
        for ( MemberRoom memberRoom : memberRoomList ) {
            list.add( fillLikeRooms( memberRoom ) );
        }
        return list;
    }

    default MemberDto.LikeRoomResponseDtos fillLikeRooms(MemberRoom memberRoom) {
        if ( memberRoom == null ) {
            return null;
        }
        MemberDto.LikeRoomResponseDtos likeRoomResponseDtos = new MemberDto.LikeRoomResponseDtos();
        likeRoomResponseDtos.setMemberId(memberRoom.getMember().getMemberId());
        likeRoomResponseDtos.setTitle(memberRoom.getRoom().getTitle());
        likeRoomResponseDtos.setImageUrl(memberRoom.getRoom().getImageUrl());
        likeRoomResponseDtos.setAdminNickname(memberRoom.getMember().getNickname());
        likeRoomResponseDtos.setMemberMaxCount(memberRoom.getRoom().getMemberMaxCount());
        likeRoomResponseDtos.setMemberCurrentCount(memberRoom.getRoom().getMemberCurrentCount());
        likeRoomResponseDtos.setPrivate(memberRoom.getRoom().isPrivate());
        likeRoomResponseDtos.setCreatedAt(memberRoom.getCreatedAt());
        likeRoomResponseDtos.setCreatedAt(memberRoom.getLastModifiedAt());
        likeRoomResponseDtos.setTags( getRoomTags(memberRoom.getRoom().getRoomTagList()));
        return likeRoomResponseDtos;
    }

    default List<MemberRoomTagDtos> getRoomTags(List<RoomTag> roomTagList){
        if( roomTagList == null ) {
            return null;
        }
        List<MemberRoomTagDtos> list = new ArrayList<>( roomTagList.size() );
        for( RoomTag roomTag : roomTagList ) {
            list.add( fillRoomTags( roomTag ) );
        }
        return list;
    }

    default MemberRoomTagDtos fillRoomTags(RoomTag roomTag){
        MemberRoomTagDtos memberRoomTagDtos = new MemberRoomTagDtos();
        memberRoomTagDtos.setTagId(roomTag.getTag().getTagId());
        memberRoomTagDtos.setRoomId(roomTag.getRoom().getRoomId());
        memberRoomTagDtos.setName(roomTag.getTag().getName());
        return memberRoomTagDtos;
    }





    //Todo : 생성한채팅방 목록
    default List<MemberDto.CreatedRoomResponseDtos> memberToCreatedResponseDtos(List<MemberRoom> memberRoomList){
        if ( memberRoomList == null ) {
            return null;
        }

        List<MemberDto.CreatedRoomResponseDtos> list = new ArrayList<MemberDto.CreatedRoomResponseDtos>( memberRoomList.size() );
        for ( MemberRoom memberRoom : memberRoomList ) {
            list.add( fillCreatedRooms( memberRoom ) );
        }
        return list;
    }

    default MemberDto.CreatedRoomResponseDtos fillCreatedRooms(MemberRoom memberRoom) {
        if ( memberRoom == null ) {
            return null;
        }
        MemberDto.CreatedRoomResponseDtos createdRoomResponseDtos = new MemberDto.CreatedRoomResponseDtos();
        createdRoomResponseDtos.setMemberId(memberRoom.getMember().getMemberId());
        createdRoomResponseDtos.setTitle(memberRoom.getRoom().getTitle());
        createdRoomResponseDtos.setImageUrl(memberRoom.getRoom().getImageUrl());
        createdRoomResponseDtos.setAdminNickname(memberRoom.getRoom().getAdminNickname());
        createdRoomResponseDtos.setMemberMaxCount(memberRoom.getRoom().getMemberMaxCount());
        createdRoomResponseDtos.setMemberCurrentCount(memberRoom.getRoom().getMemberCurrentCount());
        createdRoomResponseDtos.setPrivate(memberRoom.getRoom().isPrivate());
        createdRoomResponseDtos.setCreatedAt(memberRoom.getCreatedAt());
        createdRoomResponseDtos.setLastModifiedAt(memberRoom.getLastModifiedAt());
        createdRoomResponseDtos.setTags( getRoomTags(memberRoom.getRoom().getRoomTagList()) );
        return createdRoomResponseDtos;
    }





    //Todo : 채팅방 방문기록 목록
    default List<MemberDto.RecordRoomResponseDtos> memberToRecordResponseDtos(List<MemberRoom> memberRoomList){
        if ( memberRoomList == null ) {
            return null;
        }
        List<MemberDto.RecordRoomResponseDtos> list = new ArrayList<MemberDto.RecordRoomResponseDtos>( memberRoomList.size() );
        for ( MemberRoom memberRoom : memberRoomList ) {
            list.add( fillRecordRooms( memberRoom ) );
        }
        return list;
    }

    default MemberDto.RecordRoomResponseDtos fillRecordRooms(MemberRoom memberRoom){
        if ( memberRoom == null ) {
            return null;
        }
        MemberDto.RecordRoomResponseDtos recordRoomResponseDtos = new MemberDto.RecordRoomResponseDtos();
        recordRoomResponseDtos.setMemberId(memberRoom.getMember().getMemberId());
        recordRoomResponseDtos.setTitle(memberRoom.getRoom().getTitle());
        recordRoomResponseDtos.setImageUrl(memberRoom.getRoom().getImageUrl());
        recordRoomResponseDtos.setAdminNickname(memberRoom.getRoom().getAdminNickname());
        recordRoomResponseDtos.setMemberMaxCount(memberRoom.getRoom().getMemberMaxCount());
        recordRoomResponseDtos.setMemberCurrentCount(memberRoom.getRoom().getMemberCurrentCount());
        recordRoomResponseDtos.setPrivate(memberRoom.getRoom().isPrivate());
        recordRoomResponseDtos.setCreatedAt(memberRoom.getRoom().getCreatedAt());
        recordRoomResponseDtos.setLastModifiedAt(memberRoom.getRoom().getLastModifiedAt());
        recordRoomResponseDtos.setTags( getRoomTags(memberRoom.getRoom().getRoomTagList()));
        return recordRoomResponseDtos;
    }


}
