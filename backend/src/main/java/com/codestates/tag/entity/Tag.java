package com.codestates.tag.entity;

import com.codestates.member.entity.MemberTag;
import com.codestates.room.entity.RoomTag;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToMany(mappedBy = "tag") //cascade = CascadeType.PERSIST
    private List<MemberTag> memberTagList = new ArrayList<>();

    @JsonIgnore //추가
    @OneToMany(mappedBy = "tag")
    private List<RoomTag> roomTagList = new ArrayList<>();
}
