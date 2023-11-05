package com.codestates.common.base;

import com.codestates.tag.dto.TagDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BaseDto {
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FillRoomResponseDtos {
        private Long roomId;
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
        @JsonProperty("is_favorite")
        private boolean isFavorite;
        private List<TagDto.TagResponseDto> tags;

        public FillRoomResponseDtos(Long roomId, String title, String info, int memberMaxCount, int memberCurrentCount, String imageUrl, boolean isPrivate, String password, int favoriteCount, boolean isFavorite) {
            this.roomId = roomId;
            this.title = title;
            this.info = info;
            this.memberMaxCount = memberMaxCount;
            this.memberCurrentCount = memberCurrentCount;
            this.imageUrl = imageUrl;
            this.isPrivate = isPrivate;
            this.password = password;
            this.favoriteCount = favoriteCount;
            this.isFavorite = isFavorite;
        }
    }
}