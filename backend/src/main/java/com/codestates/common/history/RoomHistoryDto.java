package com.codestates.common.history;

import com.codestates.member.dto.MemberRoomTagDtos;
import com.codestates.member.entity.MemberRoom;
import com.codestates.tag.dto.TagDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.checkerframework.checker.units.qual.A;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RoomHistoryDto {

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
    private int favoriteCount;
    @JsonProperty("favorite_status")
    private MemberRoom.Favorite favoriteStatus;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    private  List<TagDto.TagResponseDto> tags;
}

