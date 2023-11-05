package com.codestates.favorite;

import com.codestates.tag.dto.TagDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoriteDto {
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Post {
        private long roomId;
        private long memberId;
        @JsonProperty("is_favorite")
        private boolean isFavorite;
    }

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FavoritesResponsDtos {
        private long roomId;
        private String title;
        private String info;
        @JsonProperty("image_url")
        private String imageUrl;
        private int maxCount;
        @JsonProperty("is_private")
        private boolean isPrivate;
        private String password;
        private int favoriteCount;
        @JsonProperty("is_favorite")
        private boolean isFavorite;
        private List<TagDto.TagResponseDto> tags;
    }
}
