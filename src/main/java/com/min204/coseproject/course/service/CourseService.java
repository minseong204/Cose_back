package com.min204.coseproject.course.service;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import java.util.List;

public interface CourseService {
    CourseResponseDto createCourse(CoursePostDto coursePostDto);
    CourseResponseDto updateCourse(Long courseId, CoursePostDto coursePostDto);
    CourseResponseDto findCourse(Long courseId);
    List<CourseResponseDto> findCourses(int page, int size);
    void deleteCourse(Long courseId);
}