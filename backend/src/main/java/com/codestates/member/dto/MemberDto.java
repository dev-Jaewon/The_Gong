package com.codestates.member.dto;

import com.codestates.member.entity.MemberRoom;
import com.codestates.member.entity.MemberTag;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Builder
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
    public static class PatchNickname {
        private long memberId; //추가
        private String nickname;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchImage {
        private long memberId;
        @JsonProperty("image_url")
        private String imageUrl;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchPassword {
        private long memberId;
        private String password; //기존비번
        private String newPassword;
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
        private List<MemberTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchImageResponseDto {
        private long memberId;
        @JsonProperty("image_url")
        private String imageUrl;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LikeRoomResponseDtos {
        @NotBlank
        private long roomId;
        private String title;
        private String info;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private int favoriteCount;
        @JsonProperty("favorite_status")
        private MemberRoom.Favorite favoriteStatus;
        private List<MemberRoomTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CreatedRoomResponseDtos {
        private long roomId;
        private String title;
        private String info;
        private int memberMaxCount;
        private int memberCurrentCount;
        @JsonProperty("image_url")
        private String imageUrl;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private int favoriteCount;
        @JsonProperty("favorite_status")
        private MemberRoom.Favorite favoriteStatus;
        private List<MemberRoomTagDtos> tags;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CheckNickname {
        private String nickname;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DeleteMember {
        private long memberId;
    }
}