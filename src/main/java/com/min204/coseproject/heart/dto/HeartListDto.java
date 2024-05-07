package com.min204.coseproject.heart.dto;

import com.min204.coseproject.constant.HeartType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class HeartListDto {
    private String title;
    private Long contentId;
    private HeartType heartType;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
