package com.codestates.member.repository;

import com.codestates.member.entity.MemberTag;
import com.codestates.tag.entity.Tag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberTagRepository extends JpaRepository<MemberTag, Long> {

    @EntityGraph(attributePaths = {"tag"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<MemberTag> findByTag(Tag tag);
}
