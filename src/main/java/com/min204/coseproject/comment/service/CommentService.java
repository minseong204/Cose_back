package com.min204.coseproject.comment.service;

import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.service.ContentService;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ContentService contentService;

    private final UserServiceImpl userServiceImpl;

    public Comment createComment(
            Comment comment,
            Long contentId) {
        Content content = contentService.findContent(contentId);
        User user = userServiceImpl.getLoginMember();

        comment.setUser(user);
        comment.setContent(content);

        return commentRepository.save(comment);
    }


    /*
     * 댓글 수정
     * */
    public Comment updateComment(
            Comment comment,
            Long commentId
    ) {
        Comment findComment = findVerifiedComment(commentId);
        Optional.ofNullable(comment.getBody())
                .ifPresent(findComment::setBody);

        return commentRepository.save(findComment);
    }

    public Comment findComment(long commentId) {
        return findVerifiedComment(commentId);
    }

    public Page<Comment> findComments(int page, int size) {
        return commentRepository.findAll(PageRequest.of(page, size,
                Sort.by("commentId").descending()));
    }

    /*
     * 댓글 삭제
     * */
    public void deleteComment(long commentId) {
        Comment findComment = findVerifiedComment(commentId);
        commentRepository.delete(findComment);
    }

    public Comment findVerifiedComment(long commentId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment findComment =
                optionalComment.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        return findComment;
    }

}
