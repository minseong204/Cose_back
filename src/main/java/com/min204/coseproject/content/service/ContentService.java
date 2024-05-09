package com.min204.coseproject.content.service;

import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.dto.ContentAllResponseDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.mapper.ContentMapper;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.course.service.CourseService;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.response.SingleResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.user.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ContentService {
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final UserServiceImpl userServiceImpl;
    private final ContentMapper contentMapper;
    private final CommentRepository commentRepository;
    private final CourseService courseService;
    private final CourseRepository courseRepository;

    public ContentService(UserRepository userRepository, ContentRepository contentRepository, UserServiceImpl userServiceImpl, ContentMapper contentMapper, CommentRepository commentRepository, CourseService courseService, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.contentRepository = contentRepository;
        this.userServiceImpl = userServiceImpl;
        this.contentMapper = contentMapper;
        this.commentRepository = commentRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
    }

    public Content createContent(Content content) {
        content.setUser(userServiceImpl.getLoginMember());

        return contentRepository.save(content);
    }

    public Content updateContent(Content content) {
        Content findContent = findVerifiedContent(content.getContentId());

        Optional.ofNullable(content.getTitle())
                .isPresent();

        courseService.deleteCourse(findContent);
        findContent.setCourses(courseService.createCourses(content.getCourses()));

        return contentRepository.save(findContent);
    }

    public Content findContent(Long contentId) {
        return findVerifiedContent(contentId);
    }

    public Page<Content> findContents(int page, int size) {
        return contentRepository.findAll(PageRequest.of(page, size,
                Sort.by("contentId").descending()));
    }

    public void deleteContent(Long contentId) {
        Content findContent = findVerifiedContent(contentId);
        contentRepository.delete(findContent);
    }

    public User findVerifiedUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User findUser =
                optionalUser.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return findUser;
    }

    public Content findVerifiedContent(Long contentId) {
        Optional<Content> optionalContent = contentRepository.findById(contentId);
        Content findContent =
                optionalContent.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));

        return findContent;
    }

    public Content updateViewCount(Content content) {
        return contentRepository.save(content);
    }

    @Transactional(readOnly = true)
    public ResponseEntity detail(Content content) {
        ContentAllResponseDto response = contentMapper.contentToContentAllResponse(content, commentRepository, courseRepository);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK
        );
    }

}
