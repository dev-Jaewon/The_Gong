package com.codestates.favorite;

import com.codestates.auth.jwt.custom.CheckUserPermission;
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
    @CheckUserPermission
    public ResponseEntity postFavorite(@Valid @RequestBody FavoriteDto.Post requestBody,
                                       @PathVariable("room-id") @Positive long roomId) {

       requestBody.setRoomId(roomId); //이 로직이 필요한 이유
       service.changeFavoriteStatus(roomId, requestBody.getMemberId(), requestBody.isFavorite());
       return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/members/{member-id}/like")
    //@CheckUserPermission request가 아닌경우
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