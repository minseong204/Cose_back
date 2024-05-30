package com.min204.coseproject.course.service;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

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

    public Course updateCourse(Long courseId, Course course) {
        Course findCourse = findVerifiedCourse(courseId);

        Optional.ofNullable(course.getDescription()).ifPresent(findCourse::setDescription);
        Optional.ofNullable(course.getPlaces()).ifPresent(findCourse::setPlaces);

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

    private Course findVerifiedCourse(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        return optionalCourse.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));
    }
}
