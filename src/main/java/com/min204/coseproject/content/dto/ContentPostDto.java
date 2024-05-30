package com.min204.coseproject.content.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ContentPostDto {
    @NotBlank(message = "게시글 제목을 입력해야 합니다.")
    private String title;

    private Long courseId;
}
