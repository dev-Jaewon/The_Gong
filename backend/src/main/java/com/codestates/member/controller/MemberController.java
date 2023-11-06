package com.codestates.member.controller;

import com.codestates.auth.jwt.custom.CheckUserPermission;
import com.codestates.auth.utils.ErrorResponse;
import com.codestates.common.base.BaseDto;
import com.codestates.common.image.S3ImageService;
import com.codestates.common.response.MultiResponseDto;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.dto.MemberTagDtos;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;
    private final S3ImageService s3ImageService;


    @PostMapping("/add")
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.Post requestBody) {
        Member member = mapper.postDtoToMember(requestBody);
        ResponseEntity checkMember = memberService.verifyExistsCheck(member.getEmail());

        if (checkMember != null) return checkMember;
        String profile = s3ImageService.getDefaultProfileImage();
        memberService.createMember(member, profile);
        return new ResponseEntity<>(profile, HttpStatus.CREATED);
    }


    //닉네임 중복체크
    @PostMapping("/check")
    public ResponseEntity checkNickname(@Valid @RequestBody MemberDto.CheckNickname requestBody) {
        ResponseEntity checkNickname = memberService.verifyExistsNickname(requestBody.getNickname());

        if (checkNickname != null) return checkNickname;
        return new ResponseEntity(requestBody.getNickname(), HttpStatus.OK);
    }



    @PatchMapping("/{member-id}/profile/edit")
    @CheckUserPermission
    public ResponseEntity patchMemberImage(@PathVariable("member-id") @Positive long memberId,
                                           @Valid @RequestBody MemberDto.PatchImage requestBody) {

        Member member = mapper.patchImageDtoToMember(requestBody);
        Member responseMember = memberService.updateMemberImage(member, memberId);
        return new ResponseEntity<>(mapper.memberToPatchImageResponseDto(responseMember), HttpStatus.OK);
    }


    @PatchMapping("/{member-id}/nickname/edit")
    @CheckUserPermission
    public ResponseEntity patchMemberNickname(@PathVariable("member-id") long memberId,
                                              @Valid @RequestBody MemberDto.PatchNickname requestBody) {

        // requestBody.setMemberId(memberId);
        Member member = mapper.patchNicknameDtoToMember(requestBody);
        ResponseEntity checkMember = memberService.verifyExistsNickname(member.getNickname());

        if (checkMember != null) return checkMember;
        Member responseMember = memberService.updateMemberNickname(member, memberId);
        return new ResponseEntity<>(mapper.memberToPatchResponseDto(responseMember), HttpStatus.OK);
    }


    @PatchMapping("/{member-id}/password/edit")
    @CheckUserPermission
    public ResponseEntity patchMemberPassword(@PathVariable("member-id") @Positive long memberId,
                                              @Valid @RequestBody MemberDto.PatchPassword requestBody) {

        ResponseEntity<ErrorResponse> checkMemberPassword = memberService.checkPassword(requestBody, memberId);
        if (checkMemberPassword != null) return checkMemberPassword;
        memberService.updateMemberPassword(requestBody, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{member-id}/created")
    //@CheckUserPermission requestBody가 아닌경우
    public ResponseEntity getCreatedRoom(@PathVariable("member-id") @Positive long memberId,
                                         @RequestParam(value = "page", defaultValue = "1") @Positive int page,
                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size,
                                         Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if (jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Page<MemberRoom> memberRoomPage = memberService.findCreatedRooms(page - 1, size, memberId);

        if (memberRoomPage == null || memberRoomPage.isEmpty())
            return new ResponseEntity<>(
                    new MultiResponseDto<>(new ArrayList<>(), Page.empty()), HttpStatus.OK);

        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<BaseDto.FillRoomResponseDtos> responseDtosList = mapper.memberToCreatedResponseDtos(memberRoomList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage), HttpStatus.OK);
    }


    //회원탈퇴
    @DeleteMapping("/{member-id}")
    @CheckUserPermission
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       @Valid @RequestBody MemberDto.DeleteMember requestBody) {

        memberService.removeUser(requestBody.getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    //관심태그
    @GetMapping("{member-id}/add/myTag")
    //@CheckUserPermission requestBody가 아닌경우
    public ResponseEntity addMyTag(@PathVariable("member-id") @Positive long memberId,
                                   @RequestParam("name") String name,
                                   Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        Member findMember = memberService.findMember(memberId);

        if (jwtMemberId != findMember.getMemberId()) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        List<MemberTagDtos> responseDtos = memberService.selectMyTag(name, findMember.getMemberId());
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }


    @GetMapping("/{member-id}/myTag")
    //@CheckUserPermission requestBody가 아닌경우
    public ResponseEntity getMyTag(@PathVariable("member-id") @Positive long memberId, Authentication authentication) {
        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
        Member findMember = memberService.findMember(memberId);

        if (jwtMemberId != findMember.getMemberId()) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        List<MemberTagDtos> responseDtos = memberService.findMyTagList(findMember);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}
