package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.member.repository.MemberRepository;
import com.codestates.member.repository.MemberRoomRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoomRepository memberRoomRepository;
   // private final PasswordEncoder passwordEncoder;


    public MemberService(MemberRepository memberRepository, MemberRoomRepository memberRoomRepository) {
        this.memberRepository = memberRepository;
        this.memberRoomRepository = memberRoomRepository;
    }


    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
       // String encryptedPassword = passwordEncoder.encode(member.getPassword());
       //  member.setPassword(encryptedPassword);
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }



    public Member updateMember(Member member){
        Member findMember = findVerifiedMember(member.getMemberId());
        Optional.ofNullable(member.getNickname())
                .ifPresent(nickname->findMember.setNickname(nickname));
        Optional.ofNullable(member.getImageUrl())
                .ifPresent(image->findMember.setImageUrl(image));
        Optional.ofNullable(member.getMemberTagList())
                .ifPresent(tagList-> {
                    findMember.getMemberTagList().clear();

                    for(MemberTag tag : tagList) {
                        tag.setMember(findMember);
                        findMember.getMemberTagList().add(tag);
                    }});

        return memberRepository.save(findMember);
    }




    public Member findMember(long memberId) {
        Member member = findVerifiedMember(memberId);
        return member;
    }



    public Page<Member> findMembers(int page, int size) { //전체조회사용 X
        Page<Member> memberPage = memberRepository.findAll(PageRequest.of(page,size, Sort.by("memberId").descending()));
        List<Member> memberList = memberPage.getContent();
        return new PageImpl<>(memberList, memberPage.getPageable(), memberPage.getTotalElements());
    }



    public Page<MemberRoom> findLikeRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("createdAt").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberLikeRooms = member.getMemberRoomList()

                .stream()
                .filter(memberRoom ->  memberRoom.getMember().getMemberId().equals(memberId))
                .filter(memberRoom -> memberRoom.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .collect(Collectors.toList());
        return new PageImpl<>(memberLikeRooms, pageable, memberLikeRooms.size());
    }




    public Page<MemberRoom> findCreatedRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberCreatedRooms = member.getMemberRoomList()
                .stream()
                .filter(memberRoom -> memberRoom.getAuthority().equals(MemberRoom.Authority.ADMIN))
                .collect(Collectors.toList());

        return new PageImpl<>(memberCreatedRooms, pageable, memberCreatedRooms.size());
    }



    public Page<MemberRoom> findRecordRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberRecordRooms = member.getMemberRoomList()
                .stream()
                .filter(memberRoom -> memberRoom.getHistory().equals(MemberRoom.History.VISITED))
                .collect(Collectors.toList());

        if(memberRecordRooms == null || memberRecordRooms.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return new PageImpl<>(memberRecordRooms, pageable, memberRecordRooms.size());
    }



    public void removeUser(long memberId) {
        Member member = findVerifiedMember(memberId);
        member.setStatus(Member.MemberStatus.DELETE);
        memberRepository.save(member);
    }



    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXIST);
    }



    public Member findVerifiedMember(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member findMember = member.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if(findMember.getStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return findMember;
    }

    public Member findVerifiedMember(String nickname) {
        Optional<Member> member = memberRepository.findByNickname(nickname);
        Member findMember = member.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if(findMember.getStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return findMember;
    }
}
