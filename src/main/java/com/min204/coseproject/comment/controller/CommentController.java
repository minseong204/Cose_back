package com.min204.coseproject.comment.controller;

import com.min204.coseproject.comment.dto.CommentPatchDto;
import com.min204.coseproject.comment.dto.CommentPostDto;
import com.min204.coseproject.comment.dto.CommentResponseDto;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.comment.mapper.CommentMapper;
import com.min204.coseproject.comment.service.CommentService;
import com.min204.coseproject.response.MultiResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    /*
     * 댓글 생성
     * */
    @PostMapping
    public ResponseEntity postComment(@Valid @RequestBody CommentPostDto requestBody) {
        Comment comment = commentService.createComment(
                commentMapper.commentPostDtoToComment(requestBody),
                requestBody.getContentId()
        );
        CommentResponseDto commentResponseDto = commentMapper.commentToCommentResponseDto(comment);

        return new ResponseEntity(commentResponseDto, HttpStatus.CREATED);
    }

    /*
     * 댓글 수정
     * */
    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@Valid @RequestBody CommentPatchDto requestBody,
                                       @PathVariable("commentId") @Positive Long commentId) {
        Comment comment = commentService.updateComment(
                commentMapper.commentPatchDtoToComment(requestBody),
                commentId
        );

        comment.setCommentId(commentId);
        CommentResponseDto userResponseDto = commentMapper.commentToCommentResponseDto(comment);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    /*
     * 댓글 조회
     * */
    @GetMapping("/{commentId}")
    public ResponseEntity getComment(@PathVariable("commentId") @Positive Long commentId) {
        Comment comment = commentService.findComment(commentId);
        CommentResponseDto commentResponse = commentMapper.commentToCommentResponseDto(comment);

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    /*
     * 댓글 전체 조회
     * */
    @GetMapping
    public ResponseEntity getComments(@Positive @RequestParam("page") int page,
                                      @Positive @RequestParam("size") int size) {
        Page<Comment> pageComments = commentService.findComments(page - 1, size);
        List<Comment> comments = pageComments.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(commentMapper.commentsToCommentResponseDtos(comments),
                        pageComments),
                HttpStatus.OK
        );
    }

    /*
     * 댓글 삭제
     * */
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") @Positive Long commentId) {
        commentService.deleteComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
