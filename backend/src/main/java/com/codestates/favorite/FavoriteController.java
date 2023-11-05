package com.codestates.favorite;

import com.codestates.common.base.BaseDto;
import com.codestates.common.response.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/favorites") 프론트에서 주소변경을 해주실 수 없으므로 기존주소 활용위해 제거
public class FavoriteController {
    private final FavoriteService service;
    private final FavoriteMapper mapper;

    @PostMapping("/rooms/{room-id}/favorite")
    public ResponseEntity postFavorite(@Valid @RequestBody FavoriteDto.Post requestBody,
                                       @PathVariable("room-id") @Positive long roomId) {

       requestBody.setRoomId(roomId); //이 로직이 필요한 이유
       service.changeFavoriteStatus(roomId, requestBody.getMemberId(), requestBody.isFavorite());
       return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/members/{member-id}/like")
    public ResponseEntity getFavorites(@PathVariable("member-id") @Positive long memberId,
                                       @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                       @RequestParam(value = "size", defaultValue = "5") @Positive int size) {

        Page<Favorite> favoritePage = service.getFavorites(page - 1, size, memberId);
        if(favoritePage.isEmpty())
            return new ResponseEntity<>(new MultiResponseDto<>(new ArrayList<>(), Page.empty()), HttpStatus.OK);

        List<BaseDto.FillRoomResponseDtos> responsDtos = mapper.favoriteToResponseDto(favoritePage.getContent(), memberId);
        return new ResponseEntity<>(new MultiResponseDto<>(responsDtos, favoritePage), HttpStatus.OK);
    }
}










//개선전 '찜' 로직
//    @PostMapping("/{room-id}/favorite")
//    public ResponseEntity postFavorite(@PathVariable("room-id") @Positive long roomId,
//                                       @Valid @RequestBody RoomDto.PostFavorite requestBody,
//                                       Authentication authentication) {
//
//        if(authentication==null){
//            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST, "로그인이 필요한 서비스입니다.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//        }
//        requestBody.setRoomId(roomId);
//        Room room = mapper.PostFavoriteDtoToRoom(requestBody);
//
//        if(requestBody.isFavorite()) {
//            roomService.addFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
//            return new ResponseEntity<>(HttpStatus.OK);
//        }
//        roomService.undoFavorite(room, requestBody.isFavorite(), requestBody.getMemberId());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



//    @GetMapping("/{member-id}/like")
//    public ResponseEntity getLikeRooms(@PathVariable("member-id") @Positive long memberId,
//                                       @RequestParam(value = "page", defaultValue = "1") @Positive int page,
//                                       @RequestParam(value = "size", defaultValue = "10") @Positive int size,
//                                       Authentication authentication) {
//
//        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
//        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
//
//        if (jwtMemberId != (memberId)) {
//            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//        }
//
//        Page<MemberRoom> memberRoomPage = memberService.findLikeRooms(page - 1, size, memberId);
//        if (memberRoomPage == null || memberRoomPage.isEmpty()) {
//            return new ResponseEntity<>(
//                    new MultiResponseDto<>(new ArrayList<>(), Page.empty()),
//                    HttpStatus.OK
//            );
//        }
//
//        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
//        List<MemberDto.FillRoomResponseDtos> responseDtosList = mapper.memberToLikeResponseDtos(memberRoomList, memberId);
//
//        return new ResponseEntity<>(
//                new MultiResponseDto<>(responseDtosList, memberRoomPage), HttpStatus.OK);
//    }

