package com.codestates.tag.dto;

import com.codestates.common.base.BaseDto;
import com.codestates.common.response.PageInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TagCustomResponseDto {
    private String name;
    private List<BaseDto.FillRoomResponseDtos> data;
    private PageInfo pageInfo;

    public TagCustomResponseDto(String name, List<BaseDto.FillRoomResponseDtos> data, Page page) {
        this.name = name;
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber()+1, page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
