package com.min204.coseproject.content.dto;

import com.min204.coseproject.course.dto.CourseResponseDto;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ContentAllResponseDto {

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long contentId;

    private String email;

    @NotBlank(message = "게시글 제목을 입력해야합니다.")
    private String title;

    private int heartCount;
    private int viewCount;

    private String nickName;

    private String image;

    private List<CourseResponseDto> courses;
}