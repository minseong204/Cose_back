package com.min204.coseproject.course.service;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CoursePreviewDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import java.util.List;

public interface CourseService {
    CourseResponseDto createCourse(CoursePostDto coursePostDto);
    CourseResponseDto updateCourse(Long courseId, CoursePostDto coursePostDto);
    void updatePreviewImagePathById(Long courseId, String previewImagePath);
    CourseResponseDto findCourse(Long courseId);
    List<CourseResponseDto> findCourses(int page, int size);

    List<CoursePreviewDto> findPreviewCourses(int page, int size);
    void deleteCourse(Long courseId);
}