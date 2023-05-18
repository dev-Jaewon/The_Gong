package com.codestates.member.mapper;

import com.codestates.auth.utils.ErrorResponse;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberTag;
import com.codestates.tag.dto.TagDto;
import com.codestates.tag.entity.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class unusedMapper {
//    Todo : 회원생성 응답
//    default MemberDto.PostResponseDto memberToPostResponseDto(Member createMember) {
//        if (createMember == null) {
//            return null;
//        }
//        MemberDto.PostResponseDto postResponseDto = new MemberDto.PostResponseDto();
//
//        if (createMember.getMemberId() != null) {
//            postResponseDto.setMemberId(createMember.getMemberId());
//        }
//        postResponseDto.setMemberId(createMember.getMemberId());
//        postResponseDto.setEmail(createMember.getEmail());
//        postResponseDto.setNickname(createMember.getNickname());
//        postResponseDto.setFavoriteCount(createMember.getFavoriteCount());
//        postResponseDto.setCreationCount(createMember.getCreatedCount());
//        postResponseDto.setRecordRoomCount(createMember.getRecordeCount());
//        postResponseDto.setCreatedAt(createMember.getCreatedAt());
//        postResponseDto.setLastModifiedAt(createMember.getLastModifiedAt());
//        return postResponseDto;
//    }



//      Todo : 회원조회 응답
//      default MemberDto.GetResponseDto memberToGetResponseDto(Member member) {
//        if (member == null) {
//            return null;
//        }
//        MemberDto.GetResponseDto getResponseDto = new MemberDto.GetResponseDto();
//
//        if (member.getMemberId() != null) {
//            getResponseDto.setMemberId(member.getMemberId());
//        }
//        getResponseDto.setMemberId(member.getMemberId());
//        getResponseDto.setNickname(member.getNickname());
//        getResponseDto.setEmail(member.getEmail());
//        getResponseDto.setImageUrl(member.getImageUrl());
//        getResponseDto.setFavoriteCount(member.getFavoriteCount());
//        getResponseDto.setCreationCount(member.getCreatedCount());
//        getResponseDto.setRecordRoomCount(member.getRecordeCount());
//        getResponseDto.setCreatedAt(member.getCreatedAt());
//        getResponseDto.setLastModifiedAt(member.getLastModifiedAt());
//        getResponseDto.setTags(getMemberTags(member.getMemberTagList()));
//        return getResponseDto;
//    }




//    Todo : 전체채팅방조회 사용 X
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





    //    Todo : 회원조회
//    @GetMapping("/{member-id}")
//    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId,
//                                    Authentication authentication) {
//
//        if(authentication == null) {
//            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, "미인증 사용자 : 토큰이 존재하지 않습니다.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//        }
//
//        Member member = memberService.findMember(memberId);
//        return new ResponseEntity<>(mapper.memberToGetResponseDto(member), HttpStatus.OK);
//    }



//    Todo : 회원전체조회
//    @GetMapping
//    public ResponseEntity getMembers(@Positive @RequestParam("page") int page,
//                                     @Positive @RequestParam("size") int size) {
//        Page<Member> memberPage = memberService.findMembers(page-1, size);
//        List<Member> memberList = memberPage.getContent();
//        List<MemberDto.GetResponseDtos> responseDtosList = mapper.memberToGetResponseDtos(memberList);
//
//        return new ResponseEntity<>(
//                new MultiResponseDto<>(responseDtosList, memberPage), HttpStatus.OK);
//    }







//    @PatchMapping("/{member-id}/favoriteTag/edit")
//    public ResponseEntity patchFavoriteTags(@PathVariable("member-id") @Positive long memberId,
//                                            @Valid @RequestBody TagDto.Patch requestBody,
//                                            Authentication authentication) {
//
//        Map<String, Object> principal = (Map) authentication.getPrincipal();
//        long jwtMemberId = ((Number) principal.get("memberId")).longValue();
//
//        if (jwtMemberId != requestBody.getMemberId()) {
//            ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.FORBIDDEN, "권한이 없는 사용자입니다.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
//        }
//        requestBody.setMemberId(memberId);
//        Member member = mapper.PatchDtoToFavoriteMemberTag(requestBody);
//        member = tagService.updateTags(requestBody.getTagId(),member);
//        return new ResponseEntity<>(mapper.tagToFavoriteTagResponseDto(member.getMemberTagList()),HttpStatus.OK);
//    }





//    private final MemberTagRepository memberTagRepository;
//
//
//    public Member updateTags(long tagId, Member member) {
//        Member findMember = memberService.findVerifiedMember(member.getMemberId());
//
//        Tag findTag = findVerifiedTag(tagId);
//        List<MemberTag> memberTagList = member.getMemberTagList();
//
//        if(memberTagList != null) {
//            for(MemberTag memberTag : memberTagList){
//                memberTag.setTag(findTag);
//                memberTag.setMember(member);
//                memberTagRepository.save(memberTag);
//            }
//        }
//        return memberRepository.save(member);
//    }


//
//    default Member PatchDtoToFavoriteMemberTag(TagDto.Patch requestBody){
//        if ( requestBody == null ) {return null;}
//        Member member = new Member();
//        member.setMemberId( requestBody.getMemberId() );
//        return member;
//    }
//
//
//    default List<TagDto.TagNameResponseDto> tagToFavoriteTagResponseDto(List<MemberTag> memberTagList) {
//        if (memberTagList == null) {return null;}
//        List<TagDto.TagNameResponseDto> list = new ArrayList<>(memberTagList.size());
//        for (MemberTag memberTag : memberTagList) {
//            list.add(getMemberFavoriteTag(memberTag));
//        } return list;
//    }
//
//    default TagDto.TagNameResponseDto getMemberFavoriteTag(MemberTag memberTag){
//        if(memberTag == null){return null;}
//        TagDto.TagNameResponseDto responseDto = new TagDto.TagNameResponseDto();
//        responseDto.setName(memberTag.getTag().getName());
//
//        return responseDto;
//    }

}
