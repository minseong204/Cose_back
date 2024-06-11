package com.min204.coseproject.scrap.dto;

import com.min204.coseproject.content.dto.ContentDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ScrapDto {
    private Long scrapId;
    private ContentDto content;
}