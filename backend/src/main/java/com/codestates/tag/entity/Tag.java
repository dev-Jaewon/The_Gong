package com.codestates.tag.entity;

import com.codestates.member.entity.MemberTag;
import com.codestates.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<MemberTag> memberTagList = new ArrayList<>();
}
