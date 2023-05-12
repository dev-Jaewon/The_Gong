package com.codestates.room.mapper;

import com.codestates.member.entity.MemberRoom;
import com.codestates.room.dto.RoomDto;
import com.codestates.room.dto.RoomTagDtos;
import com.codestates.room.dto.RoomUserDtos;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {

    Room postDtoToRoom(RoomDto.Post requestBody);
    Room patchDtoToRoom(RoomDto.Patch requestBody);




    //Todo : 찜하기 & 찜취소
    default Room PostFavoriteDtoToRoom(RoomDto.PostFavorite requestBody){
        if ( requestBody == null ) {
            return null;
        }
        Room room = new Room();
        room.setRoomId( requestBody.getRoomId() );
        room.setFavoriteCount(room.getFavoriteCount() );
        return room;
    }





    //Todo : 방생성 응답
    default RoomDto.PostResponseDto roomToPostResponseDto(Room createRoom){
        if ( createRoom == null ) {
            return null;
        }
        RoomDto.PostResponseDto postResponseDto = new RoomDto.PostResponseDto();

        if ( createRoom.getRoomId() != null ) {
            postResponseDto.setRoomId( createRoom.getRoomId() );
        }
        postResponseDto.setTitle( createRoom.getTitle() );
        postResponseDto.setInfo( createRoom.getInfo() );

        if ( createRoom.getAdminMemberId() != null ) {
            postResponseDto.setAdminMemberId( createRoom.getAdminMemberId() );
        }
        postResponseDto.setImageUrl( createRoom.getImageUrl() );
        postResponseDto.setMemberMaxCount( createRoom.getMemberMaxCount() );
        postResponseDto.setMemberCurrentCount( createRoom.getMemberCurrentCount() );
        postResponseDto.setPrivate( createRoom.isPrivate() );
        postResponseDto.setPassword( createRoom.getPassword() );
        postResponseDto.setFavoriteCount( createRoom.getFavoriteCount());
        postResponseDto.setTags( getRoomTags(createRoom.getRoomTagList()) );
        return postResponseDto;
    }

    default List<RoomTagDtos> getRoomTags(List<RoomTag> roomTagList){
        return roomTagList.stream()
                .map(memberRoom -> {
                    RoomTagDtos roomTagDtos = new RoomTagDtos();
                    roomTagDtos.setName(memberRoom.getTag().getName());
                    return roomTagDtos;
                })
                .collect(Collectors.toList());
    }

    default List<RoomUserDtos> getRoomParticipants(List<MemberRoom> memberRoomList){
        return memberRoomList.stream()
                .map(memberRoom -> {
                    RoomUserDtos roomUserDtos = new RoomUserDtos();
                    roomUserDtos.setMemberId(memberRoom.getMember().getMemberId());
                    roomUserDtos.setNickname(memberRoom.getMember().getNickname());
                    roomUserDtos.setImageUrl(memberRoom.getMember().getImageUrl());
                    return roomUserDtos;
                })
                .collect(Collectors.toList());
    }





    //Todo : 방장권한 위임 & 응답
    default Room patchAdminDtoToRoom(RoomDto.PatchAdmin requestBody){
        if (requestBody == null) {
            return null;
        }
        Room room = new Room();
        room.setRoomId(requestBody.getRoomId());
        room.setAdminMemberId(requestBody.getNewAdminId());
        return room;
    }

    default RoomDto.PatchAdminResponseDto roomToPatchAdminResponseDto(Room room){
        if ( room == null ) {
            return null;
        }
        RoomDto.PatchAdminResponseDto patchAdminResponseDto = new RoomDto.PatchAdminResponseDto();

        if ( room.getRoomId() != null ) {
            patchAdminResponseDto.setRoomId(room.getRoomId());
        }
        patchAdminResponseDto.setAdminMemberId(room.getAdminMemberId());
        patchAdminResponseDto.setAdminNickname(room.getAdminNickname());
        patchAdminResponseDto.setImageUrl(room.getImageUrl());
        return patchAdminResponseDto;
    }





    //Todo : 방정보수정 응답
    default RoomDto.PatchResponseDto roomToPatchResponseDto(Room room){
        if (room == null) {
            return null;
        }
        RoomDto.PatchResponseDto patchResponseDto = new RoomDto.PatchResponseDto();

        if (room.getRoomId() != null) {
            patchResponseDto.setRoomId(room.getRoomId());
        }
        patchResponseDto.setTitle(room.getTitle());
        patchResponseDto.setInfo(room.getInfo());
        patchResponseDto.setAdminNickname(room.getAdminNickname());
        patchResponseDto.setImageUrl(room.getImageUrl());
        patchResponseDto.setMemberMaxCount(room.getMemberMaxCount());
        patchResponseDto.setMemberCurrentCount(room.getMemberCurrentCount());
        patchResponseDto.setPrivate(room.isPrivate());

        if(room.isPrivate() == true) {
            patchResponseDto.setPassword( room.getPassword());
        }
        patchResponseDto.setFavoriteCount(room.getFavoriteCount());
        patchResponseDto.setTags(getRoomTags(room.getRoomTagList()));
        patchResponseDto.setParticipantList(getRoomParticipants(room.getMemberRoomList()));
        return patchResponseDto;
    }





    //Todo : 해당방 참여자목록 응답
    default List<RoomDto.GetRoomUserResponseDtos> roomToRoomUserResponseDtos(List<MemberRoom> memberRoomList){
        if (memberRoomList == null) {
            return null;
        }
        List<RoomDto.GetRoomUserResponseDtos> list = new ArrayList<RoomDto.GetRoomUserResponseDtos>(memberRoomList.size());
        for (MemberRoom memberRoom : memberRoomList) {
            list.add(memberRoomToGetRoomUserResponseDtos(memberRoom));
        }
        return list;
    }

    default RoomDto.GetRoomUserResponseDtos memberRoomToGetRoomUserResponseDtos(MemberRoom memberRoom) {
        if ( memberRoom == null ) {
            return null;
        }
        RoomDto.GetRoomUserResponseDtos getRoomUserResponseDtos = new RoomDto.GetRoomUserResponseDtos();
        getRoomUserResponseDtos.setRoomId(memberRoom.getRoom().getRoomId());
        getRoomUserResponseDtos.setMemberId(memberRoom.getMember().getMemberId());
        getRoomUserResponseDtos.setNickname(memberRoom.getMember().getNickname());
        getRoomUserResponseDtos.setImageUrl(memberRoom.getMember().getImageUrl());
        getRoomUserResponseDtos.setAuthority(memberRoom.getAuthority());

        return getRoomUserResponseDtos;
    }





    //Todo : 최신순 방목록조회 응답
    default List<RoomDto.GetNewRoomResponseDtos> roomToNewRoomResponseDtos(List<Room> roomList){
        if (roomList == null) {return null;}

        List<RoomDto.GetNewRoomResponseDtos> list = new ArrayList<RoomDto.GetNewRoomResponseDtos>(roomList.size());
        for ( Room room : roomList ) {
            list.add(roomToGetNewRoomResponseDtos(room));
        }
        return list;
    }

    default RoomDto.GetNewRoomResponseDtos roomToGetNewRoomResponseDtos(Room room) {
        if (room == null) {return null;}

        RoomDto.GetNewRoomResponseDtos getNewRoomResponseDtos = new RoomDto.GetNewRoomResponseDtos();

        if ( room.getRoomId() != null ) {
            getNewRoomResponseDtos.setRoomId(room.getRoomId());
        }
        getNewRoomResponseDtos.setTitle(room.getTitle());
        getNewRoomResponseDtos.setInfo(room.getInfo());
        getNewRoomResponseDtos.setImageUrl(room.getImageUrl());
        getNewRoomResponseDtos.setMemberMaxCount(room.getMemberMaxCount());
        getNewRoomResponseDtos.setMemberCurrentCount(room.getMemberCurrentCount());
        getNewRoomResponseDtos.setPrivate(room.isPrivate());
        getNewRoomResponseDtos.setFavoriteCount(room.getFavoriteCount());
        getNewRoomResponseDtos.setFavoriteStatus(getRoomFavorite(room, room.getMemberRoomList()));
        getNewRoomResponseDtos.setTags(getRoomTags(room.getRoomTagList()));
        return getNewRoomResponseDtos;
    }

    default MemberRoom.Favorite getRoomFavorite(Room room, List<MemberRoom> memberRoomList){
        if (memberRoomList == null) {
            return null;
        }
        MemberRoom memberRoom = memberRoomList.stream().filter(r -> r.getRoom().getRoomId().equals(room.getRoomId())).findFirst().get();
        return memberRoom.getFavorite();
    }


    default List<RoomDto.RoomAdminDto> getRoomAdmin(Room room){
        if(room == null) {
            return null;
        }
        List<RoomDto.RoomAdminDto> responseDtoList = new ArrayList<>();
        RoomDto.RoomAdminDto roomAdminDto = new RoomDto.RoomAdminDto();
        roomAdminDto.setMemberId(room.getAdminMemberId());
        roomAdminDto.setAdminNickname(room.getAdminNickname());
        responseDtoList.add(roomAdminDto);
        return responseDtoList;
    }





    //Todo : 추천순 방목록조회 응답
    List<RoomDto.GetRecommendRoomResponseDtos> roomToRecommendRoomResponseDtos(List<Room> roomList);





    //Todo : 검색 응답
    RoomDto.SearchResponseDto roomToSearchResponseDtos (Page<Room> searchResult);
//    Page<RoomDto.SearchResponseDto> roomToSearchResponseDtos(Page<Room> searchResult);
}
