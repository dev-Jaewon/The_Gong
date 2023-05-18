package com.codestates.tag.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.common.sort.SortMethod;
import com.codestates.room.entity.Room;
import com.codestates.room.entity.RoomTag;
import com.codestates.tag.entity.Tag;
import com.codestates.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;


    //Todo : 태그조회
    public Page<Room> findTagSortedByFavorite(int page, int size, long tagId, String sort) {
        Tag findRoomtag = findVerifiedTag(tagId);
        findRoomtag.setTagId(tagId);

        List<Room> findRoomList = null;

        if (sort != null && sort.equals("favoriteCount")) {
            findRoomList = findRoomtag.getRoomTagList().stream()
                    .filter(rt -> rt.getRoom().getRoomTagList().contains(rt))
                    .map(RoomTag::getRoom)
                    .sorted(SortMethod.sortByFavoriteCount())
                    .collect(Collectors.toList());
        }
        else if (sort != null && sort.equals("oldRoom")) {
            findRoomList = findRoomtag.getRoomTagList().stream()
                    .filter(rt -> rt.getRoom().getRoomTagList().contains(rt))
                    .map(RoomTag::getRoom)
                    .sorted(SortMethod.sortByOld())
                    .collect(Collectors.toList());
        }
        else if (sort != null && sort.equals("newRoom")) {
            findRoomList = findRoomtag.getRoomTagList().stream()
                    .filter(rt -> rt.getRoom().getRoomTagList().contains(rt))
                    .map(RoomTag::getRoom)
                    .sorted(SortMethod.sortByNew())
                    .collect(Collectors.toList());
        }
        Pageable pageable = PageRequest.of(page, size);

        if(findRoomtag == null || findRoomList.isEmpty()) return new PageImpl<>(new ArrayList<>(),pageable,0);

        return new PageImpl<>(findRoomList, pageable, findRoomList.size());
    }



    //Todo : 태그전체조회
    public Page<Tag> findTags(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("tagId").descending());
        return tagRepository.findAll(pageable);
    }



    //Todo : DB 체크 메서드
    public Tag findVerifiedTag(long tagId) {
        Optional<Tag> optionalTag = tagRepository.findById(tagId);
        Tag findTag = optionalTag.orElseThrow(() -> new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND));
        return findTag;
    }
}
