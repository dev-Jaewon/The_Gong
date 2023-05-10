package com.codestates.room.dto;

import com.codestates.member.entity.MemberRoom;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RoomDto {

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Post {
        @JsonProperty("admin_member_id")
        private long adminMemberId;
        private String title;
        private String info; //info 제거유무
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private List<RoomTagDtos> tags;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PostFavorite {
        private long roomId;
        private long memberId;
        @JsonProperty("is_favorite")
        private boolean isFavorite;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PostUndoFavorite {
        private long roomId;
        private long memberId;
        @JsonProperty("is_favorite")
        private boolean isFavorite;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PostResponseDto {
        private long roomId;
        private String title;
        private String info; //info 제거유무
        @JsonProperty("admin_member_id")
        private long adminMemberId;
        @JsonProperty("admin_nickname")
        private String adminNickname;
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
        private List<RoomUserDtos> participantList;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Patch {
        private long roomId;
        @JsonProperty("admin_member_id")
        private long adminMemberId;
        private String title;
        private String info; //info 제거여부
        private String notice;
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private List<RoomTagDtos> tags;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchResponseDto {
        private long roomId;
        private String title;
        private String info; //info 제거여부
        @JsonProperty("admin_nickname")
        private String adminNickname; //adminMemberId 추가여부 체크
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
        private List<RoomUserDtos> participantList;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchAdmin {
        private long roomId;
        @JsonProperty("new_admin_id")
        private long newAdminId;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PatchAdminResponseDto {
        private long roomId;
        private String title;
        private String info; //info 제거여부
        @JsonProperty("admin_nickname")
        private String adminNickname; //adminMemberId 추가여부 체크
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
        private List<RoomUserDtos> participantList;
    }


    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetRoomUserResponseDtos { //참여자 조회 보완필요!
        private long roomId; //방정보 필요할듯
        private long memberId;
        private String nickname;
        @JsonProperty("image_url")
        private String imageUrl;
        private MemberRoom.Authority authority; //고민
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetNewRoomResponseDtos {
        private long roomId;
        private String title;
        private String info; //제거여부
        private List<RoomDto.RoomAdminDto> admin;
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetRecommendRoomResponseDtos {
        private long roomId;
        private String title;
        private String info; //제거여부
        private List<RoomDto.RoomAdminDto> admin;
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class RoomAdminDto {
        private long memberId;
        @JsonProperty("admin_nickname")
        private String adminNickname;
//        @JsonProperty("image_url")
//        private String imageUrl;
    }



    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SearchResponseDto {
        private long roomId;
        private String title;
        private String info;
        @JsonProperty("admin_nickname")
        private String adminNickname;
        @JsonProperty("image_url")
        private String imageUrl;
        private int MemberMaxCount;
        private int MemberCurrentCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private LocalDateTime createdAt;
        private LocalDateTime lastModifiedAt;
        private int favoriteCount; //추가
        private List<RoomTagDtos> tags;
    }
}
