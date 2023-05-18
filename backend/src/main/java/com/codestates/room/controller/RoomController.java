package com.codestates.room.controller;

import com.codestates.auth.utils.ErrorResponse;
import com.codestates.common.response.MultiResponseDto;
import com.codestates.room.dto.RoomDto;
import com.codestates.room.entity.Room;
import com.codestates.room.mapper.RoomMapper;
import com.codestates.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomMapper mapper;

    //경로지정해보기
    private String thumbnail = "https://gangtaegyeong12-bucket.s3.ap-northeast-2.amazonaws.com/thumbnail.jpg";



    @PostMapping("/add")
    public ResponseEntity postRoom(@Valid @RequestBody RoomDto.Post requestBody,
                                   Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != requestBody.getAdminMemberId()) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        //기본썸네일 추가
        if(requestBody.getImageUrl()==null){
            requestBody.setImageUrl(thumbnail);
        }

        Room room = mapper.postDtoToRoom(requestBody);
        room = roomService.createRoom(room, requestBody.getAdminMemberId());
        return new ResponseEntity<>(mapper.roomToPostResponseDto(room), HttpStatus.CREATED);
    }




    @PatchMapping("/{room-id}/edit")
    public ResponseEntity patchRoom(@PathVariable("room-id") @Positive long roomId,
                                    @Valid @RequestBody RoomDto.Patch requestBody,
                                    Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != requestBody.getAdminMemberId()) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        requestBody.setRoomId(roomId);
        //log.info("비밀번호 확인 1 {}",requestBody.getPassword()); //아..
        Room room = mapper.patchDtoToRoom(requestBody);
        //log.info("비밀번호 확인 2 {}", room.getPassword());
        room = roomService.updateRoom(room, requestBody.getAdminMemberId());
        //log.info("비밀번호 확인 3 {}", room.getPassword());
        return new ResponseEntity<>(mapper.roomToPatchResponseDto(room),HttpStatus.OK);
    }




    @PatchMapping("/{room-id}/switch")
    public ResponseEntity patchAdmin(@PathVariable("room-id") @Positive long roomId,
                                     @Valid @RequestBody RoomDto.PatchAdmin requestBody,
                                     Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        Room findRoom = roomService.findVerifiedRoom(roomId);

        if (jwtMemberId != findRoom.getAdminMemberId()){
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        requestBody.setRoomId(roomId);
        Room room = mapper.patchAdminDtoToRoom(requestBody);
        room = roomService.switchAdmin(room, requestBody.getNewAdminId());
        return new ResponseEntity<>(mapper.roomToPatchAdminResponseDto(room),HttpStatus.OK);
    }




    @PostMapping("/{room-id}/favorite")
    public ResponseEntity postFavorite(@PathVariable("room-id") @Positive long roomId,
                                       @Valid @RequestBody RoomDto.PostFavorite requestBody,
                                       Authentication authentication) {

        if(authentication==null){
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "로그인이 필요한 서비스입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        requestBody.setRoomId(roomId);
        Room room = mapper.PostFavoriteDtoToRoom(requestBody);

        if(requestBody.isFavorite()) {
            roomService.addFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        roomService.undoFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @GetMapping("/new")
    public ResponseEntity getNewRooms(@RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                      @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
        Page<Room> roomPage = roomService.findNewRooms(page-1, size);
        List<Room> roomList = roomPage.getContent();
        List<RoomDto.GetNewRoomResponseDtos> responseDtosList = mapper.roomToNewRoomResponseDtos(roomList);
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
    }




    @GetMapping("/{memberId}/{sort}/recommend")
    public ResponseEntity getRecommendRooms(@PathVariable("member-id") long memberId, @PathVariable("sort") String sort,
                                            @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                            @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                            Authentication authentication) {
        if(authentication.equals(null)){
            Page<Room> roomPage = roomService.findUnauthorizedRooms(page -1, size, sort);
            List<Room> roomList = roomPage.getContent();
            List<RoomDto.GetRecommendRoomResponseDtos> responseDtosList = mapper.roomToRecommendRoomResponseDtos(roomList);
            return new ResponseEntity<>(
                    new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
        }

        Page<Room> roomPage = roomService.findRecommendRooms(page -1, size, sort, memberId);
        List<Room> roomList = roomPage.getContent();
        List<RoomDto.GetRecommendRoomResponseDtos> responseDtosList = mapper.roomToRecommendRoomResponseDtos(roomList);
        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, roomPage), HttpStatus.OK);
    }




    @DeleteMapping("/{room-id}")
    public ResponseEntity deleteRoom(@PathVariable("room-id") @Positive long roomId,
                                     @RequestParam("member") @Positive long memberId,
                                     Authentication authentication){

        Map<String, Object> principal = (Map)authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != memberId){
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        roomService.deleteRoom(roomId); //완전삭제
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
