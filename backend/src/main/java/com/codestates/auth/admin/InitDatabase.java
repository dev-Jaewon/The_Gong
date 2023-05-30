package com.codestates.auth.admin;

import com.codestates.member.entity.Member;
import com.codestates.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitDatabase {

    @Value("${default.profile.image}")
    private String profile;

    @Bean
    CommandLineRunner adminDatabase(MemberRepository repository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (repository.findByEmail("admin@example.com").isEmpty()) {
                Member admin = new Member();
                admin.setMemberId(0L);
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin1234"));
                admin.setNickname("관리자");
                admin.setStatus(Member.MemberStatus.ACTIVE);
                admin.setImageUrl(profile);
                admin.setIsAdmin(true);
                repository.save(admin);
            }
        };
    }
}

