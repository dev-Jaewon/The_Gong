package com.codestates.common.search;

import com.codestates.common.sort.SortMethod;
import com.codestates.member.entity.MemberRoom;
import com.codestates.room.dto.RoomDto;
import com.codestates.room.entity.Room;
import com.codestates.room.repository.RoomRepository;
import com.codestates.tag.dto.TagDto;
import com.codestates.tag.entity.Tag;
import com.codestates.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RoomRepository roomRepository;
    private final TagRepository tagRepository;

    public Page<RoomDto.SearchResponseDto> searchRoomTitles(int page, int size, String sort, String query) {
        Pageable pageable = PageRequest.of(page, size);
        List<Room> searchRooms = roomRepository.findAllByTitleContaining(query, pageable);
        List<Room> sortedRooms = settingSort(searchRooms, sort); // 정렬된 Room 리스트

        List<RoomDto.SearchResponseDto> searchResponseList = sortedRooms.stream()
                .map(this::createSearchResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponseList, pageable, searchRooms.size());
    }



    public Page<RoomDto.SearchResponseDto> searchRoomInfo(int page, int size, String sort, String query) {
        Pageable pageable = PageRequest.of(page, size);
        List<Room> searchRooms = roomRepository.findAllByInfoContaining(query, pageable);
        List<Room> sortedRooms = settingSort(searchRooms, sort); // 정렬된 Room 리스트

        List<RoomDto.SearchResponseDto> searchResponseList = sortedRooms.stream()
                .map(this::createSearchResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponseList, pageable, searchRooms.size());
    }



    public Page<RoomDto.SearchResponseDto> searchRoomTags(int page, int size, String sort, String query) {
        Pageable pageable = PageRequest.of(page, size);
        List<Room> searchRooms = roomRepository.findAllByRoomTagList_TagNameContaining(query, pageable);
        List<Room> sortedRooms = settingSort(searchRooms, sort); // 정렬된 Room 리스트

        List<RoomDto.SearchResponseDto> searchResponseList = sortedRooms.stream()
                .map(this::createSearchResponseDto)
                .collect(Collectors.toList());

        return new PageImpl<>(searchResponseList, pageable, searchRooms.size());
    }



    private RoomDto.SearchResponseDto createSearchResponseDto(Room room) {
        MemberRoom memberRoom = new MemberRoom();
        memberRoom.setRoom(room);

        List<TagDto.TagResponseDto> tagResponseDtos = room.getRoomTagList().stream()
                .map(tag -> new TagDto.TagResponseDto(tag.getTag().getTagId(), tag.getTag().getName()))
                .collect(Collectors.toList());

        return RoomDto.SearchResponseDto.builder()
                .roomId(room.getRoomId())
                .title(room.getTitle())
                .info(room.getInfo())
                .imageUrl(room.getImageUrl())
                .MemberMaxCount(room.getMemberMaxCount())
                .MemberCurrentCount(room.getMemberCurrentCount())
                .isPrivate(room.isPrivate())
                .favoriteStatus(memberRoom.getFavorite())
                .favoriteCount(room.getFavoriteCount())
                .tags(tagResponseDtos).build();
    }



    private List<Room> settingSort(List<Room> searchRooms, String sort) {

        if (sort != null && sort.equals("favoriteCount")) {
            return searchRooms.stream().sorted(SortMethod.sortByFavoriteCount())
                  .collect(Collectors.toList());

        } else if (sort != null && sort.equals("oldRoom")) {
            return searchRooms.stream().sorted(SortMethod.sortByOld())
                  .collect(Collectors.toList());

        } else if (sort != null && sort.equals("newRoom")) {
            return searchRooms.stream().sorted(SortMethod.sortByNew())
                   .collect(Collectors.toList());
        }
        return searchRooms;
    }



    //Todo : 태그검색
    public List<TagDto.TagSearchResponseDto> searchTag(String query) {

        List<Tag> searchTags = tagRepository.findAllByNameContaining(query);
        List<TagDto.TagSearchResponseDto> searchList = new ArrayList<>();

        for (Tag tag : searchTags) {
            searchList.add(TagDto.TagSearchResponseDto.builder()
                    .name(tag.getName()).build());
        }
        return searchList;
    }
}