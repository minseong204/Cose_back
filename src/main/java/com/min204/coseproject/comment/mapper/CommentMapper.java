package com.min204.coseproject.comment.mapper;

import com.min204.coseproject.comment.dto.CommentPatchDto;
import com.min204.coseproject.comment.dto.CommentPostDto;
import com.min204.coseproject.comment.dto.CommentResponseDto;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    default Comment commentPostDtoToComment(CommentPostDto requestBody) {
        Content content = new Content();
        content.setContentId(requestBody.getContentId());

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setBody(requestBody.getBody());

        return comment;
    }

    default Comment commentPatchDtoToComment(CommentPatchDto requestBody) {
        Content content = new Content();
        content.setContentId(requestBody.getContentId());

        Comment comment = new Comment();
        content.setContentId(requestBody.getContentId());
        comment.setContent(content);
        comment.setBody(requestBody.getBody());

        return comment;
    }

    default CommentResponseDto commentToCommentResponseDto(Comment comment) {
        User user = comment.getUser();
        Content content = comment.getContent();

        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .userId(user.getUserId())
                .contentId(content.getContentId())
                .title(content.getTitle())
                .body(comment.getBody())
                .nickName(user.getNickname())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .image(user.getImage())
                .build();
    }

    List<CommentResponseDto> commentsToCommentResponseDtos(List<Comment> comment);
}
