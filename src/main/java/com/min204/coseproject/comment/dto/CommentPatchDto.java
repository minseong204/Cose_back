package com.min204.coseproject.comment.dto;

import com.min204.coseproject.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentPatchDto {
    private long commentId;
    private long userid;
    private long contentId;

    @NotSpace(message = "내용을 입력하세요")
    private String body;

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }
}
