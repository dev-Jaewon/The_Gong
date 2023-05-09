package com.codestates.member.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.member.entity.Member;
import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.codestates.member.repository.MemberRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
   // private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }



    public Member createMember(Member member) {
        verifiExistsEmail(member.getEmail());
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
        member.setFavoriteCount(getFavoriteCount(member.getMemberId()));
        member.setCreatedCount(getCreatedCount(member.getMemberId()));
        member.setRecordeCount(getRecordRoomCount(member.getMemberId()));
        return member;
    }



    public Page<Member> findMembers(int page, int size) {
        Page<Member> members = memberRepository.findAll(PageRequest.of(page,size, Sort.by("memberId").descending()));
        List<Member> memberList = members.getContent();
        List<Member> updatedList = new ArrayList<>();

        for(Member member : memberList) {
            int favoriteCount = getFavoriteCount(member.getMemberId());
            int createdCount = getCreatedCount(member.getMemberId());
            int recordCount = getRecordRoomCount(member.getMemberId());
            member.setFavoriteCount(favoriteCount);
            member.setCreatedCount(createdCount);
            member.setRecordeCount(recordCount);
            updatedList.add(member);
        }
        return new PageImpl<>(updatedList, members.getPageable(), members.getTotalElements());
    }



    public Page<MemberRoom> findLikeRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberLikeRooms = member.getMemberRoomList()
                .stream()
                .filter(room -> !room.getFavorite().equals(MemberRoom.Favorite.LIKE))
                .collect(Collectors.toList());

        if(memberLikeRooms == null || memberLikeRooms.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return new PageImpl<>(memberLikeRooms, pageable, memberLikeRooms.size());
    }



    public Page<MemberRoom> findCreatedRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberCreatedRooms = member.getMemberRoomList()
                .stream()
                .filter(room -> !room.getMember().getMemberId().equals(member.getMemberId()))
                .collect(Collectors.toList());

        if(memberCreatedRooms == null || memberCreatedRooms.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }
        return new PageImpl<>(memberCreatedRooms, pageable, memberCreatedRooms.size());
    }



    public Page<MemberRoom> findRecordRooms(int page, int size, long memberId) {
        Pageable pageable = PageRequest.of(page,size,Sort.by("memberId").descending());
        Member member = findVerifiedMember(memberId);

        List<MemberRoom> memberRecordRooms = member.getMemberRoomList()
                .stream()
                .filter(room -> !room.getHistory().equals(MemberRoom.History.VISITED))
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



    private void verifiExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent())
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXIST);
    }



    private Member findVerifiedMember(long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        Member findMember = member.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        if(findMember.getStatus().equals(Member.MemberStatus.DELETE)) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        return findMember;
    }



    private int getFavoriteCount(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);

        if(findMember.isPresent()) {
            Member member = findMember.get();
            List<MemberRoom> memberRoomList = member.getMemberRoomList();
            int favoriteRoomCount = (int) memberRoomList.stream()
                    .filter(room -> room.getFavorite().equals(MemberRoom.Favorite.NONE)).count();
            return favoriteRoomCount;
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }



    private int getCreatedCount(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isPresent()) {
            Member member = findMember.get();
            List<MemberRoom> memberRoomList = member.getMemberRoomList();
            int createdRoomCount = (int) memberRoomList.stream()
                    .filter(room -> !room.getMember().getMemberId().equals(member.getMemberId())).count();
            return createdRoomCount;
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }



    private int getRecordRoomCount(Long memberId) {
        Optional<Member> findMember = memberRepository.findById(memberId);
        if(findMember.isPresent()) {
            Member member = findMember.get();
            List<MemberRoom> memberRoomList = member.getMemberRoomList();
            int recordRoomCount = (int) memberRoomList.stream()
                    .filter(room -> !room.getHistory().equals(MemberRoom.History.VISITED)).count();
            return recordRoomCount;
        } else {
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
    }

}
