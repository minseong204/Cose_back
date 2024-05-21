package com.min204.coseproject.content.dto;

import com.min204.coseproject.course.dto.CourseResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ContentResponseDto {
    private Long contentId;
    private String email;

    @NotBlank(message = "게시글 제목을 입력해야 합니다.")
    private String title;

    private int heartCount;
    private int viewCount;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private List<CourseResponseDto> courses;
}