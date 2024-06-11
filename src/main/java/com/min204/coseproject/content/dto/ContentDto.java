package com.min204.coseproject.content.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContentDto {
    private Long contentId;
    private String title;
    private int viewCount;
}