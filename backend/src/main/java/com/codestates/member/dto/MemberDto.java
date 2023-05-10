package com.codestates.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class MemberDto {

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Post {
        private String email;
        private String nickname;
        private String password;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Patch {
        private long memberId;
        private String nickname;
        @JsonProperty("image_url")
        private String imageUrl;
        private List<MemberTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PostResponseDto {
        private long memberId;
        private String email;
        private String nickname;
//      private String imageUrl;
        private int favoriteCount;
        private int creationCount;
        private int recordRoomCount;//참여했던 채팅방
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchResponseDto {
        private long memberId;
        private String nickname;
        private String email;
        @JsonProperty("image_url")
        private String imageUrl;
        private int favoriteCount;
        private int creationCount;
        private int recordRoomCount;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<MemberTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponseDto {
        private long memberId;
        private String nickname;
        private String email;
        @JsonProperty("image_url")
        private String imageUrl;
        private int favoriteCount;
        private int creationCount;
        private int recordRoomCount;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<MemberTagDtos> tags;
    }


//    @Getter
//    @Setter //회원전체조회 사용 X
//    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//    public static class GetResponseDtos {
//        private long memberId;
//        private String nickname;
//        private String email;
//        @JsonProperty("image_url")
//        private String imageUrl;
//        private int favoriteCount;
//        private int creationCount;
//        private int recordRoomCount;
//        private LocalDateTime createdAt;
//        private LocalDateTime lastModifiedAt;
//        private List<MemberTagDtos> tags;
//    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LikeRoomResponseDtos {
        @NotBlank
        private long roomId;
        private String title;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("admin_nickname")
        private String adminNickname; // 화면에 방장이름
        private int favoriteCount;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<MemberRoomTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreatedRoomResponseDtos {
        private long roomId;
        private String title;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("admin_nickname")
        private String adminNickname; // 화면에 방장이름
        private int favoriteCount;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<MemberRoomTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RecordRoomResponseDtos {
        private long roomId;
        private String title;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("admin_nickname")
        private String adminNickname; // 화면에 방장이름
        private int favoriteCount;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private List<MemberRoomTagDtos> tags;
    }
}
