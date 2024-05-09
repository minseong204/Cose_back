package com.min204.coseproject.course.service;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.repository.CourseRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Content content) {
        Long contentId = content.getContentId();

        List<Course> courses = courseRepository.findAllByContentId(contentId);

        courses.stream().forEach(course -> courseRepository.delete(course));
    }

    public Course updateCourse(Long courseId, Course course) {
        Course findCourse = findVerifiedCourse(courseId);

        Optional.ofNullable(course.getBody()).ifPresent(findCourse::setBody);

        return courseRepository.save(findCourse);
    }

    public List<Course> findCourses() {
        return courseRepository.findAll();
    }

    public Page<Course> findCourses(int page, int size) {
        return courseRepository.findAll(PageRequest.of(page, size, Sort.by("placeId").descending()));
    }

    public List<Course> findCoursesByContentId(Long contentId) {
        return courseRepository.findAllByContentId(contentId);
    }

    public List<Course> createCourses(List<Course> courses) {
        return courses.stream().map(course -> courseRepository.save(course)).collect(Collectors.toList());
    }

    public Course findCourse(Long courseId) {
        return findVerifiedCourse(courseId);
    }

    public void deleteCourse(Long courseId) {
        Course findCourse = findVerifiedCourse(courseId);
        courseRepository.delete(findCourse);
    }

    public Course findVerifiedCourse(Long courseId) {
        Optional<Course> optionalCourse = courseRepository.findByCourseId(courseId);
        Course findCourse = optionalCourse.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COURSE_NOT_FOUND));

        return findCourse;
    }
}
