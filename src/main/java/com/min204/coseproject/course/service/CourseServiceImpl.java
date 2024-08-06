package com.min204.coseproject.course.service;

import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CoursePreviewDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.entity.CourseUser;
import com.min204.coseproject.course.repository.CourseUserRepository;
import com.min204.coseproject.place.entity.Place;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;
    private final CourseUserService courseUserService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CourseResponseDto createCourse(CoursePostDto coursePostDto) {
        Course course = courseMapper.coursePostDtoToCourse(coursePostDto);
        User currentUser = userService.getLoginMember();

        // CourseUser 생성 및 관계 설정
        CourseUser courseUser = CourseUser.builder()
                .course(course)
                .user(currentUser)
                .editPermission(CourseUser.EditPermission.ADMIN)
                .build();

        // Course에 CourseUser 추가 (양방향 관계 설정)
        course.addCourseUser(courseUser);

        // Course 저장 (CascadeType.ALL로 인해 CourseUser도 함께 저장됨)
        Course savedCourse = courseRepository.save(course);

        // CourseResponseDto로 변환하여 반환
        return courseMapper.courseToCourseResponseDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponseDto updateCourse(Long courseId, CoursePostDto coursePostDto) {
        Course findCourse = findVerifiedCourse(courseId);

        // 기존 장소를 명시적으로 제거
        for (Place place : findCourse.getPlaces()) {
            entityManager.remove(place);
        }
        findCourse.getPlaces().clear();
        entityManager.flush();

        // 새로운 장소 추가
        List<Place> updatedPlaces = coursePostDto.getPlaces().stream()
                .map(placeDto -> {
                    Place place = courseMapper.placeDtoToPlace(placeDto);
                    place.setCourse(findCourse);
                    return place;
                })
                .collect(Collectors.toList());

        for (int i = 0; i < updatedPlaces.size(); i++) {
            updatedPlaces.get(i).setPlaceOrder(i + 1);
        }

        findCourse.setPlaces(updatedPlaces);
        Course updatedCourse = courseRepository.save(findCourse);

        return courseMapper.courseToCourseResponseDto(updatedCourse);
    }

    public void updatePreviewImagePathById(Long courseId, String previewImagePath) {
        Course course = findVerifiedCourse(courseId);
        course.setPreviewImagePath(previewImagePath);
        courseRepository.save(course);
    }

    @Override
    public CourseResponseDto findCourse(Long courseId) {
        Course course = courseRepository.findByIdWithPlacesAndUsers(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        return courseMapper.courseToCourseResponseDto(course);
    }


    @Override
    public List<CourseResponseDto> findCourses(int page, int size) {
        Page<Course> pageCourses = courseRepository.findAll(PageRequest.of(page - 1, size, Sort.by("courseId").descending()));
        return courseMapper.coursesToCourseResponseDtos(pageCourses.getContent());
    }

    @Override
    public List<CoursePreviewDto> findPreviewCourses(int page, int size) {
        Page<Course> pageCourses = courseRepository.findAll(PageRequest.of(page - 1, size, Sort.by("courseId").descending()));
        return courseMapper.coursesToCoursePreviewResponseDtos(pageCourses.getContent());
    }

    @Override
    public void deleteCourse(Long courseId) {
        Course findCourse = findVerifiedCourse(courseId);
        courseRepository.delete(findCourse);
    }

    private Course findVerifiedCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new BusinessLogicException(ErrorCode.COURSE_NOT_FOUND));
    }
}
