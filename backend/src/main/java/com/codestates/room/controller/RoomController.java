package com.codestates.room.controller;

import com.codestates.common.response.MultiResponseDto;
import com.codestates.member.entity.MemberRoom;
import com.codestates.room.dto.RoomDto;
import com.codestates.room.entity.Room;
import com.codestates.room.mapper.RoomMapper;
import com.codestates.room.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper mapper;

    public RoomController(RoomService roomService, RoomMapper mapper) {
        this.roomService = roomService;
        this.mapper = mapper;
    }


    @PostMapping("/add")
    public ResponseEntity postRoom(@Valid @RequestBody RoomDto.Post requestBody) {
        Room room = mapper.postDtoToRoom(requestBody);
        room = roomService.createRoom(room, requestBody.getAdminMemberId());
        return new ResponseEntity<>(mapper.roomToPostResponseDto(room), HttpStatus.CREATED);
    }



    @PatchMapping("/{room-id}/edit")
    public ResponseEntity patchRoom(@PathVariable("room-id") @Positive long roomId,
                                    @Valid @RequestBody RoomDto.Patch requestBody) {
        requestBody.setRoomId(roomId);
        Room room = mapper.patchDtoToRoom(requestBody);
        room = roomService.updateRoom(room, requestBody.getAdminMemberId());
        return new ResponseEntity<>(mapper.roomToPatchResponseDto(room),HttpStatus.OK);
    }



    @PatchMapping("/{room-id}/switch")
    public ResponseEntity patchAdmin(@PathVariable("room-id") @Positive long roomId,
                                     @Valid @RequestBody RoomDto.PatchAdmin requestBody) {
       requestBody.setRoomId(roomId);
       Room room = mapper.patchAdminDtoToRoom(requestBody);
       room = roomService.switchAdmin(room, requestBody.getNewAdminId());
       return new ResponseEntity<>(mapper.roomToPatchAdminResponseDto(room),HttpStatus.OK);
    }



    @DeleteMapping("/{room-id}")
    public ResponseEntity deleteRoom(@PathVariable("room-id") @Positive long roomId){
        roomService.deleteRoom(roomId); //완전삭제 or 상태여부
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping("/{room-id}/favorite")
    public ResponseEntity postFavorite(@PathVariable("room-id") @Positive long roomId,
                                       @Valid @RequestBody RoomDto.PostFavorite requestBody) {
        requestBody.setRoomId(roomId);
        Room room = mapper.PostFavoriteDtoToRoom(requestBody);
        roomService.addFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @PostMapping("/{room-id}/undo_favorite")
    public ResponseEntity postUndoFavorite(@PathVariable("room-id") @Positive long roomId,
                                           @Valid @RequestBody RoomDto.PostUndoFavorite requestBody) {
        requestBody.setRoomId(roomId);
        Room room = mapper.PostUndoDtoToRoom(requestBody);
        roomService.undoFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/{room-id}/users")
    public ResponseEntity getRoomUsers(@PathVariable("room-id") @Positive long roomId,
                                       @Positive @RequestParam("page") int page,
                                       @Positive @RequestParam("size") int size) {
        Page<MemberRoom> memberPage = roomService.findRoomUsers(page-1, size, roomId);
        List<MemberRoom> memberList = memberPage.getContent();
        List<RoomDto.GetRoomUserResponseDtos> responseDtosList = mapper.roomToRoomUserResponseDtos(memberList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList,memberPage), HttpStatus.OK);
    }



    @GetMapping("/new")
    public ResponseEntity getNewRooms(@Positive @RequestParam("page") int page,
                                      @Positive @RequestParam("size") int size) {
        Page<Room> roomPage = roomService.findNewRooms(page-1, size);
        List<Room> roomList = roomPage.getContent();
        List<RoomDto.GetNewRoomResponseDtos> responseDtosList = mapper.roomToNewRoomResponseDtos(roomList);
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
    }



    @GetMapping("/recommend")
    public ResponseEntity getRecommendRooms(@Positive @RequestParam("page") int page,
                                            @Positive @RequestParam("size") int size) {
        Page<Room> roomPage = roomService.findRecommendRooms(page -1, size);
        List<Room> roomList = roomPage.getContent();
        List<RoomDto.GetRecommendRoomResponseDtos> responseDtosList = mapper.roomToRecommendRoomResponseDtos(roomList);
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
    }



    @GetMapping("/{keyword}")
    public ResponseEntity<RoomDto.SearchResponseDto> getSearchRoom(@PathVariable("keyword") String keyword,
                                                                   @RequestParam(name = "page", defaultValue = "1") int page,
                                                                   @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<Room> searchResult = roomService.searchRoomsByKeyword(keyword, PageRequest.of(page -1, size));
        RoomDto.SearchResponseDto responseDtos = mapper.roomToSearchResponseDtos(searchResult);
        return ResponseEntity.ok(responseDtos);
    }

}
