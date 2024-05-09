package com.min204.coseproject.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponseDto {
    private long commentId;
    private String email;
    private long contentId;

    @NotBlank(message = "게시글 제목을 입력하세요")
    private String title;

    private String body;

    private String nickName;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String image;
}
