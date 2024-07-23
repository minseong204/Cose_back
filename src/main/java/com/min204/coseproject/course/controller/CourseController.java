package com.min204.coseproject.course.controller;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CoursePreviewDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.service.CourseServiceImpl;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.constant.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseServiceImpl courseService;

    @PostMapping
    public ResponseEntity<?> postCourse(@Valid @RequestBody CoursePostDto requestBody) {
        CourseResponseDto courseResponseDto = courseService.createCourse(requestBody);
        return CoseResponse.toResponse(SuccessCode.COURSE_CREATED, courseResponseDto, HttpStatus.CREATED.value());
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity<?> patchCourse(@Valid @RequestBody CoursePostDto requestBody,
                                         @PathVariable("courseId") Long courseId) {
        CourseResponseDto courseResponseDto = courseService.updateCourse(courseId, requestBody);
        return CoseResponse.toResponse(SuccessCode.COURSE_UPDATED, courseResponseDto, HttpStatus.OK.value());
    }

    @PatchMapping("/image/{courseId}")
    public ResponseEntity<?> patchPreviewImagePathToCourse(@Valid @RequestBody String previewImagePath,
                                                           @PathVariable("courseId") Long courseId) {
        courseService.updatePreviewImagePathById(courseId, previewImagePath);
        return CoseResponse.toResponse(SuccessCode.COURSE_UPDATED, courseId.toString(), HttpStatus.OK.value());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getCourse(@PathVariable("courseId") Long courseId) {
        CourseResponseDto courseResponseDto = courseService.findCourse(courseId);
        return CoseResponse.toResponse(SuccessCode.FETCH_SUCCESS, courseResponseDto, HttpStatus.PARTIAL_CONTENT.value());
    }

    @GetMapping
    public ResponseEntity<?> getCourses(@RequestParam("page") int page,
                                        @RequestParam("size") int size) {
        List<CoursePreviewDto> courses = courseService.findPreviewCourses(page, size);
        return CoseResponse.toResponse(SuccessCode.FETCH_SUCCESS, courses, HttpStatus.PARTIAL_CONTENT.value());
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.deleteCourse(courseId);
        return CoseResponse.toResponse(SuccessCode.COURSE_DELETED,  courseId.toString(), HttpStatus.OK.value());
    }
}