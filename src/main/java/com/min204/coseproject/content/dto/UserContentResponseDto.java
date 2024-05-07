package com.min204.coseproject.content.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserContentResponseDto {
    private Long contentId;

    @NotBlank(message = "게시글 제목 입력해야합니다.")
    private String title;

    private List<ContentCourseResponseDto> courses;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
