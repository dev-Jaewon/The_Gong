package com.codestates.member.controller;

import com.codestates.auth.utils.ErrorResponse;
import com.codestates.common.response.MultiResponseDto;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.mapper.MemberMapper;
import com.codestates.member.service.MemberService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;


@Validated
@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper mapper;

    public MemberController(MemberService memberService, MemberMapper mapper) {
        this.memberService = memberService;
        this.mapper = mapper;
    }


    @PostMapping("/add")
    public ResponseEntity postMember (@Valid @RequestBody MemberDto.Post requestBody) {
        Member member = mapper.postDtoToMember(requestBody);
        ResponseEntity checkMember = memberService.verifyExistsCheck(member.getEmail(), member.getNickname());
        if(checkMember!=null){return checkMember;}

        memberService.createMember(member);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @PatchMapping("/{member-id}/nickname/edit")
    public ResponseEntity patchMemberNickname(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberDto.PatchNickname requestBody,
                                      Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        // requestBody.setMemberId(memberId);
        Member member = mapper.patchNicknameDtoToMember(requestBody);
        ResponseEntity checkMember = memberService.verifyExistsNickname(member.getNickname());
        if(checkMember != null){return checkMember;}

        Member responseMember = memberService.updateMemberNickname(member, memberId);
        return new ResponseEntity<>(mapper.memberToPatchResponseDto(responseMember),HttpStatus.OK);
    }



    @PatchMapping("/{member-id}/image/edit")
    public ResponseEntity patchMemberImage(@PathVariable("member-id") @Positive long memberId,
                                           @Valid @RequestBody MemberDto.PatchImage requestBody,
                                           Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        Member member = mapper.patchImageDtoToMember(requestBody);
        Member responseMember = memberService.updateMemberImage(member, memberId);
        return new ResponseEntity<>(mapper.memberToPatchImageResponseDto(responseMember),HttpStatus.OK);
    }




    @PatchMapping("/{member-id}/password/edit")
    public ResponseEntity patchMemberPassword(@PathVariable("member-id") @Positive long memberId,
                                      @Valid @RequestBody MemberDto.PatchPassword requestBody,
                                      Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        ResponseEntity<ErrorResponse> checkMemberPassword = memberService.checkPassword(requestBody, memberId);
        if(checkMemberPassword != null){return checkMemberPassword;}
        memberService.updateMemberPassword(requestBody, memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    @GetMapping("/{member-id}/like")
    public ResponseEntity getLikeRooms(@Positive @RequestParam("page") int page,
                                       @Positive @RequestParam("size") int size,
                                       @PathVariable("member-id") @Positive long memberId,
                                       Authentication authentication) {

        Map<String, Object> principal = (Map<String, Object>) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Page<MemberRoom> memberRoomPage = memberService.findLikeRooms(page -1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.LikeRoomResponseDtos> responseDtosList = mapper.memberToLikeResponseDtos(memberRoomList, memberId);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }




    @GetMapping("/{member-id}/created")
    public ResponseEntity getCreatedRoom(@Positive @RequestParam("page") int page,
                                         @Positive @RequestParam("size") int size,
                                         @PathVariable("member-id") @Positive long memberId,
                                         Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Page<MemberRoom> memberRoomPage = memberService.findCreatedRooms(page-1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.CreatedRoomResponseDtos> responseDtosList = mapper.memberToCreatedResponseDtos(memberRoomList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }




    @GetMapping("/{member-id}/record")
    public ResponseEntity getRecordRoom(@Positive @RequestParam("page") int page,
                                        @Positive @RequestParam("size") int size,
                                        @PathVariable("member-id") @Positive long memberId,
                                        Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if(jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Page<MemberRoom> memberRoomPage = memberService.findRecordRooms(page-1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.RecordRoomResponseDtos> responseDtosList = mapper.memberToRecordResponseDtos(memberRoomList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }




    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       Authentication authentication) {

        Map<String, Object> principal = (Map) authentication.getPrincipal();
        long jwtMemberId = ((Number) principal.get("memberId")).longValue();

        if (jwtMemberId != (memberId)) {
            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자 입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
        memberService.removeUser(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{member-id}/like")
    public ResponseEntity getLikeRooms(@PathVariable("member-id") @Positive long memberId,
                                       @Positive @RequestParam("page") int page,
                                       @Positive @RequestParam("size") int size) {

        Page<MemberRoom> memberRoomPage = memberService.findLikeRooms(page -1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.LikeRoomResponseDtos> responseDtosList = mapper.memberToLikeResponseDtos(memberRoomList, memberId);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }



    @GetMapping("/{member-id}/created")
    public ResponseEntity getCreatedRoom(@PathVariable("member-id") @Positive long memberId,
                                         @Positive @RequestParam("page") int page,
                                         @Positive @RequestParam("size") int size) {
        Page<MemberRoom> memberRoomPage = memberService.findCreatedRooms(page -1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.CreatedRoomResponseDtos> responseDtosList = mapper.memberToCreatedResponseDtos(memberRoomList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }



    @GetMapping("/{member-id}/record")
    public ResponseEntity getRecordRoom(@PathVariable("member-id") @Positive long memberId,
                                        @Positive @RequestParam("page") int page,
                                        @Positive @RequestParam("size") int size) {
        Page<MemberRoom> memberRoomPage = memberService.findRecordRooms(page -1, size, memberId);
        List<MemberRoom> memberRoomList = memberRoomPage.getContent();
        List<MemberDto.RecordRoomResponseDtos> responseDtosList = mapper.memberToRecordResponseDtos(memberRoomList);

        return new ResponseEntity<>(
                new MultiResponseDto<>(responseDtosList, memberRoomPage) , HttpStatus.OK);
    }



    @DeleteMapping("/{member-id}")
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId) {
        memberService.removeUser(memberId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}




