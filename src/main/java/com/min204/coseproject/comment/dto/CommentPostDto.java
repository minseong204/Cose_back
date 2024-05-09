package com.min204.coseproject.comment.dto;

import com.min204.coseproject.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentPostDto {
    private long contentId;

    @NotSpace(message = "내용을 입력해주세요")
    private String body;
}
