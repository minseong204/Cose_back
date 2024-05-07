package com.min204.coseproject.content.dto;

import com.min204.coseproject.course.dto.CoursePostDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ContentPostDto {
    @NotBlank(message = "게시글 제목을 입력해야합니다.")
    private String title;

    @NotNull
    private List<CoursePostDto> courses;
}
