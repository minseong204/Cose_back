package com.min204.coseproject.content.dto;

import com.min204.coseproject.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ContentCourseResponseDto {
    @NotSpace(message = "장소를 채워주세요")
    private String place;
}
