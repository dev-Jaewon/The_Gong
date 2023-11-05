package com.codestates.member.mapper;

import com.codestates.common.base.BaseDto;
import com.codestates.common.base.BaseMapper;
import com.codestates.member.dto.MemberDto;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import org.mapstruct.Mapper;
import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface MemberMapper extends BaseMapper {
    Member postDtoToMember(MemberDto.Post requestBody);
    Member patchNicknameDtoToMember(MemberDto.PatchNickname requestBody);
    Member patchImageDtoToMember(MemberDto.PatchImage requestBody);

    default MemberDto.PatchImageResponseDto memberToPatchImageResponseDto(Member responseMember) {
        if (responseMember == null) return null;
        MemberDto.PatchImageResponseDto responseDto = new MemberDto.PatchImageResponseDto();
        responseDto.setMemberId(responseMember.getMemberId());
        responseDto.setImageUrl(responseMember.getImageUrl());
        return responseDto;
    }


    //Todo : 회원수정 응답
    default MemberDto.PatchResponseDto memberToPatchResponseDto(Member createMember) {
        if (createMember == null) return null;
        MemberDto.PatchResponseDto patchResponseDto = new MemberDto.PatchResponseDto();

        if (createMember.getMemberId() != null) patchResponseDto.setMemberId(createMember.getMemberId());
        patchResponseDto.setMemberId(createMember.getMemberId());
        patchResponseDto.setNickname(createMember.getNickname());
        patchResponseDto.setEmail(createMember.getEmail());
        patchResponseDto.setImageUrl(createMember.getImageUrl());
        patchResponseDto.setTags(getMemberTags(createMember.getMemberTagList()));
        return patchResponseDto;
    }


    //Todo : 생성한 채팅방 목록
    default List<BaseDto.FillRoomResponseDtos> memberToCreatedResponseDtos(List<MemberRoom> memberRoomList) {
        return memberRoomList.stream()
                .filter(memberRoom -> memberRoom.getAuthority().equals(MemberRoom.Authority.ADMIN))
                .map(memberRoom -> this.fillRoomDatas_Member(memberRoom.getRoom(), memberRoom.getMember()))
                .collect(Collectors.toList());
    }
}