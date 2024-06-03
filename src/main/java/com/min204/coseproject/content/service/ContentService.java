package com.min204.coseproject.content.service;

import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.dto.ContentAllResponseDto;
import com.min204.coseproject.content.dto.ContentPostDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.mapper.ContentMapper;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.response.SingleResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final CourseRepository courseRepository;
    private final CommentRepository commentRepository;
    private final ContentMapper contentMapper;
    private final UserService userService;

    public Content createContent(ContentPostDto requestBody) {
        Content content = contentMapper.contentPostDtoToContent(requestBody);

        User currentUser = userService.getLoginMember();
        content.setUser(currentUser);

        if (requestBody.getCourseId() != null) {
            Course course = findVerifiedCourse(requestBody.getCourseId());
            content.addCourse(course);
        }
        return contentRepository.save(content);
    }

    public void linkCourse(Long contentId, Long courseId) {
        Content content = findVerifiedContent(contentId);
        Course course = findVerifiedCourse(courseId);
        content.addCourse(course);
        contentRepository.save(content);
    }

    public Content updateContent(Content content) {
        Content findContent = findVerifiedContent(content.getContentId());

        Optional.ofNullable(content.getTitle()).ifPresent(findContent::setTitle);
        Optional.ofNullable(content.getCourses()).ifPresent(courses -> {
            findContent.getCourses().clear();
            findContent.getCourses().addAll(courses);
            courses.forEach(course -> course.setContent(findContent));
        });

        return contentRepository.save(findContent);
    }

    public Content findContent(Long contentId) {
        return findVerifiedContent(contentId);
    }

    public Page<Content> findContents(int page, int size) {
        return contentRepository.findAll(PageRequest.of(page, size, Sort.by("contentId").descending()));
    }

    public void deleteContent(Long contentId) {
        Content findContent = findVerifiedContent(contentId);
        contentRepository.delete(findContent);
    }

    public Content updateViewCount(Content content) {
        return contentRepository.save(content);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<SingleResponseDto<ContentAllResponseDto>> detail(Content content) {
        ContentAllResponseDto response = contentMapper.contentToContentAllResponse(content, commentRepository, courseRepository);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }

    private Content findVerifiedContent(Long contentId) {
        Optional<Content> optionalContent = contentRepository.findByIdWithCourses(contentId);
        return optionalContent.orElseThrow(() -> new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));
    }

    public Course findVerifiedCourse(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
    }
}
