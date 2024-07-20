package com.min204.coseproject.course.service;

import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CoursePreviewDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
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
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseUserRepository courseUserRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    @Transactional
    public CourseResponseDto createCourse(CoursePostDto coursePostDto) {
        // 코스 생성
        User currentUser = userService.getLoginMember();
        Course course = courseMapper.coursePostDtoToCourse(coursePostDto);
        course.setUser(currentUser);
        Course savedCourse = courseRepository.save(course);

        // 코스 생성자를 CourseUserMapped에 추가
        CourseUser creatorMapping = new CourseUser();
        creatorMapping.setCourse(savedCourse);
        creatorMapping.setUser(currentUser);
        creatorMapping.setEditPermission(CourseUser.EditPermission.ADMIN); // 생성자에게 ADMIN 권한 부여
        courseUserRepository.save(creatorMapping);

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

    @Override
    public CourseResponseDto findCourse(Long courseId) {
        Course course = courseRepository.findCourseWithPlaces(courseId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.COURSE_NOT_FOUND));
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
