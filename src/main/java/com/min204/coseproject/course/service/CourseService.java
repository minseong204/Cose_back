package com.min204.coseproject.course.service;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.entity.Place;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Course createCourse(CoursePostDto courseDto) {
        Course course = courseMapper.coursePostDtoToCourse(courseDto);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long courseId, CoursePostDto courseDto) {
        Course findCourse = findVerifiedCourse(courseId);

        findCourse.setDescription(courseDto.getDescription());

        // 기존 장소를 명시적으로 제거
        for (Place place : findCourse.getPlaces()) {
            entityManager.remove(place);
        }
        findCourse.getPlaces().clear();
        entityManager.flush();

        // 새로운 장소 추가
        List<Place> updatedPlaces = courseDto.getPlaces().stream()
                .map(placeDto -> {
                    Place place = courseMapper.placeDtoToPlace(placeDto);
                    place.setCourse(findCourse);
                    return place;
                })
                .collect(Collectors.toList());

        findCourse.setPlaces(updatedPlaces);

        return courseRepository.save(findCourse);
    }

    public CourseResponseDto findCourse(Long courseId) {
        Course course = courseRepository.findCourseWithPlaces(courseId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
        return courseMapper.courseToCourseResponseDto(course);
    }

    public Page<Course> findCourses(int page, int size) {
        return courseRepository.findAll(PageRequest.of(page, size, Sort.by("courseId").descending()));
    }

    public void deleteCourse(Long courseId) {
        Course findCourse = findVerifiedCourse(courseId);
        courseRepository.delete(findCourse);
    }

    public List<Course> findCoursesByContentId(Long contentId) {
        return courseRepository.findAllByContent_ContentId(contentId);
    }

    public Course findVerifiedCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
    }
}
