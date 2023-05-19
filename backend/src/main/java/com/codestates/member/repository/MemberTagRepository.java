package com.codestates.member.repository;

import com.codestates.member.entity.MemberTag;
import com.codestates.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {
    Optional<MemberTag> findByTag(Tag tag);
}
