package com.min204.coseproject.user.mapper;

import com.min204.coseproject.comment.dto.UserCommentResponse;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.dto.ContentCourseResponseDto;
import com.min204.coseproject.content.dto.UserContentResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.heart.Heart;
import com.min204.coseproject.heart.dto.HeartListDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.heart.repository.HeartRepository;
import com.min204.coseproject.user.dto.UserAllResponseDto;
import com.min204.coseproject.user.dto.UserPatchDto;
import com.min204.coseproject.user.dto.UserPostDto;
import com.min204.coseproject.user.dto.UserResponseDto;
import com.min204.coseproject.user.entity.User;
import org.mapstruct.Mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userPostDtoToUser(UserPostDto userPostDto);

    User userPatchDtoToUser(UserPatchDto userPatchDto);

    UserResponseDto userToUserResponseDto(User user);

    default UserAllResponseDto InfoResponse(User user, ContentRepository contentRepository, CommentRepository commentRepository, HeartRepository heartRepository, CourseRepository courseRepository) {
        List<Content> contents = contentRepository.findAllByUserId(user.getUserId());
        Collections.reverse(contents);
        List<Comment> comments = commentRepository.findAllByUserId(user.getUserId());
        Collections.reverse(comments);
        List<Heart> hearts = heartRepository.findAllByUserId(user.getUserId());
        Collections.reverse(hearts);

        return UserAllResponseDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .image(user.getImage())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .comments(commentsToCommentResponseDtos(comments))
                .contents(contentsToContentResponseDtos(contents, courseRepository))
                .hearts(heartsToHeartResponseDtos(hearts))
                .build();
    }

    default List<UserCommentResponse> commentsToCommentResponseDtos(List<Comment> comments) {
        return comments.stream()
                .map(comment -> UserCommentResponse.builder()
                        .commentId(comment.getCommentId())
                        .contentId(comment.getContent().getContentId())
                        .title(comment.getContent().getTitle())
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .body(comment.getBody())
                        .build())
                .collect(Collectors.toList());
    }

    default List<UserContentResponseDto> contentsToContentResponseDtos(List<Content> contents, CourseRepository courseRepository) {
        return contents.stream()
                .map(content -> UserContentResponseDto.builder()
                        .contentId(content.getContentId())
                        .title(content.getTitle())
                        .createdAt(content.getCreatedAt())
                        .modifiedAt(content.getModifiedAt())
                        .courses(coursesToCourseResponseDtos(courseRepository.findAllByContentId(content.getContentId())))
                        .build())
                .collect(Collectors.toList());
    }

    default List<ContentCourseResponseDto> coursesToCourseResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(course -> ContentCourseResponseDto.builder()
                        .place(course.getPlace())
                        .build())
                .collect(Collectors.toList());
    }

    default List<HeartListDto> heartsToHeartResponseDtos(List<Heart> hearts) {
        return hearts.stream()
                .map(heart -> HeartListDto.builder()
                        .contentId(heart.getContent().getContentId())
                        .title(heart.getContent().getTitle())
                        .heartType(heart.getHeartType())
                        .createdAt(heart.getCreatedAt())
                        .modifiedAt(heart.getModifiedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
