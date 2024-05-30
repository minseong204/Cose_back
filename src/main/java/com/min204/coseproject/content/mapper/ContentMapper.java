package com.min204.coseproject.content.mapper;

import com.min204.coseproject.comment.dto.CommentResponseDto;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.dto.ContentAllResponseDto;
import com.min204.coseproject.content.dto.ContentPatchDto;
import com.min204.coseproject.content.dto.ContentPostDto;
import com.min204.coseproject.content.dto.ContentResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.user.entity.User;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ContentMapper {

    @Autowired
    private CourseMapper courseMapper;

    public Content contentPostDtoToContent(ContentPostDto requestBody) {
        Content content = new Content();
        content.setTitle(requestBody.getTitle());
        return content;
    }

    public Content contentPatchDtoToContent(ContentPatchDto requestBody) {
        Content content = new Content();
        content.setContentId(requestBody.getContentId());
        content.setTitle(requestBody.getTitle());
        return content;
    }

    public ContentResponseDto contentToContentResponse(Content content) {
        User user = content.getUser();
        List<CourseResponseDto> courses = content.getCourses().stream()
                .map(courseMapper::courseToCourseResponseDto)
                .collect(Collectors.toList());

        return ContentResponseDto.builder()
                .contentId(content.getContentId())
                .email(user != null ? user.getEmail() : null)
                .title(content.getTitle())
                .heartCount(content.getHeartCount())
                .viewCount(content.getViewCount())
                .createdAt(content.getCreatedAt())
                .modifiedAt(content.getModifiedAt())
                .courses(courses)
                .build();
    }

    public List<ContentResponseDto> contentsToContentResponse(List<Content> contents) {
        return contents.stream()
                .map(this::contentToContentResponse)
                .collect(Collectors.toList());
    }

    public ContentAllResponseDto contentToContentAllResponse(Content content, CommentRepository commentRepository, CourseRepository courseRepository) {
        User user = content.getUser();
        List<Comment> comments = commentRepository.findAllByContentId(content.getContentId());
        Collections.reverse(comments);
        List<Course> courses = courseRepository.findAllByContent_ContentId(content.getContentId());

        return ContentAllResponseDto.builder()
                .contentId(content.getContentId())
                .email(user != null ? user.getEmail() : null)
                .nickName(user != null ? user.getNickname() : null)
                .title(content.getTitle())
                .heartCount(content.getHeartCount())
                .comments(commentsToCommentResponseDtos(comments))
                .createdAt(content.getCreatedAt())
                .modifiedAt(content.getModifiedAt())
                .courses(courseMapper.coursesToCourseResponseDtos(courses))
                .viewCount(content.getViewCount())
                .build();
    }

    private List<CommentResponseDto> commentsToCommentResponseDtos(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponseDto.builder()
                        .commentId(comment.getCommentId())
                        .contentId(comment.getContent().getContentId())
                        .email(comment.getUser().getEmail())
                        .body(comment.getBody())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .title(comment.getContent().getTitle())
                        .nickName(comment.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}
