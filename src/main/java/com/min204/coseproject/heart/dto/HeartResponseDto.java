package com.min204.coseproject.heart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HeartResponseDto {
    private Long heartId;
    private Long contentId;
    private Long userId;
    private String heartType;
}
