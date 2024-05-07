package com.min204.coseproject.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserCommentResponse {
    private long commentId;
    private long contentId;

    @NotBlank(message = "댓글 제목을 입력해야합니다.")
    private String title;

    private String body;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
