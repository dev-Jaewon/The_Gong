package com.codestates.common.delete;

import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class SchedulingConfig {

    private final MemberRepository memberRepository;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000) //24시간마다 자동실행
    public void deleteExpiredMembers(){
        LocalDateTime now = LocalDateTime.now();

        List<Member> deleteMemberList = memberRepository.findByDeletionDateBefore(now);
        if (deleteMemberList != null) {
            memberRepository.deleteAll(deleteMemberList);
        }
    }
}
