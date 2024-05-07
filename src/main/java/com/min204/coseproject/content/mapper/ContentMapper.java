package com.min204.coseproject.content.mapper;

import com.min204.coseproject.comment.dto.CommentResponseDto;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.dto.ContentAllResponseDto;
import com.min204.coseproject.content.dto.ContentPatchDto;
import com.min204.coseproject.content.dto.ContentPostDto;
import com.min204.coseproject.content.dto.ContentResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ContentMapper {

    default Content contentPostDtoToContent(ContentPostDto requestBody) {
        Content content = new Content();

        List<Course> courses = coursesDtosToCourses(requestBody.getCourses(), content);
        content.setCourses(courses);
        content.setTitle(requestBody.getTitle());

        return content;
    }

    default Content contentPatchDtoToContent(ContentPatchDto requestBody) {
        Content content = new Content();

        content.setContentId(requestBody.getContentId());
        List<Course> courses = coursesDtosToCourses(requestBody.getCourses(), content);

        content.setTitle(requestBody.getTitle());
        content.setCourses(courses);

        return content;
    }

    default ContentResponseDto contentToContentResponse(Content content) {
        User user = content.getUser();

        return ContentResponseDto.builder()
                .contentId(content.getContentId())
                .email(user.getEmail())
                .title(content.getTitle())
                .heartCount(content.getHeartCount())
                .viewCount(content.getViewCount())
                .createdAt(content.getCreatedAt())
                .modifiedAt(content.getModifiedAt())
                .courses(coursesToCourseResponseDtos(content.getCourses()))
                .build();
    }

    default List<Course> coursesDtosToCourses(List<CoursePostDto> coursePostDtos, Content content) {
        return coursePostDtos.stream().map(coursePostDto -> {
            Course course = new Course();
            course.addContent(content);
            course.setPlace(coursePostDto.getPlace());
            course.setBody(coursePostDto.getBody());
            course.setX(coursePostDto.getX());
            course.setY(coursePostDto.getY());
            course.setAddress(coursePostDto.getAddress());

            return course;
        }).collect(Collectors.toList());
    }

    default List<CourseResponseDto> coursesToCourseResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(course -> CourseResponseDto.builder()
                        .contentId(course.getContent().getContentId())
                        .place(course.getPlace())
                        .body(course.getBody())
                        .x(course.getX())
                        .y(course.getY())
                        .courseId(course.getCourseId())
                        .address(course.getAddress())
                        .build())
                .collect(Collectors.toList());
    }

    List<ContentResponseDto> contentsToContentResponse(List<Content> contents);

    default ContentAllResponseDto contentToContentAllResponse(Content content, CommentRepository commentRepository, CourseRepository courseRepository) {
        User user = content.getUser();
        List<Comment> comments = commentRepository.findAllByContentId(content.getContentId());
        Collections.reverse(comments);
        List<Course> courses = courseRepository.findAllByContentId(content.getContentId());

        return ContentAllResponseDto.builder()
                .contentId(content.getContentId())
                .email(user.getEmail())
                .nickName(user.getNickname())
                .title(content.getTitle())
                .heartCount(content.getHeartCount())
                .comments(commentsToCommentResponseDtos(comments))
                .createdAt(content.getCreatedAt())
                .modifiedAt(content.getModifiedAt())
                .courses(coursesToCourseResponseDtos(courseRepository.findAllByContentId(content.getContentId())))
                .viewCount(content.getViewCount())
                .build();
    }

    default List<CommentResponseDto> commentsToCommentResponseDtos(List<Comment> comments) {
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
