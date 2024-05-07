package com.min204.coseproject.content.dto;

import com.min204.coseproject.course.dto.CourseResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ContentResponseDto {
    private Long contentId;
    private Long userId;

    @NotBlank(message = "게시글 제목을 입력해야 합니다.")
    private String title;

    private int heartCount;
    private int viewCount;

    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CourseResponseDto> courses;
}
