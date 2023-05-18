package com.codestates.tag.dto;

import com.codestates.member.entity.MemberRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class TagDto {

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Post {
        private long tagId;
        private long memberId;
        private String name;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetRoomTagResponseDto {
        private long roomId;
        private String title;
        private String info;
        @JsonProperty("image_url")
        private String imageUrl;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private int favoriteCount;
        @JsonProperty("favorite_status")
        private MemberRoom.Favorite favoriteStatus;
        private List<TagResponseDto> tags;
    }



    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TagResponseDto {
        private long tagId;
        private String name;

    }



    @Getter
    @Setter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TagSearchResponseDto{
        private String name;
    }
}



//    @Getter
//    @Setter
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//    public static class GetTagResponseDtos {
//        private long tagId;
//        private String name;
//    }