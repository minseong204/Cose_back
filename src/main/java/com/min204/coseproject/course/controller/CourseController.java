package com.min204.coseproject.course.controller;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.service.CourseService;
import com.min204.coseproject.response.MultiResponseDto;
import com.min204.coseproject.response.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    /*
     * 코스 생성
     * */
    @PostMapping
    public ResponseEntity postCourse(@Valid @RequestBody CoursePostDto requestBody) {
        Course course = courseService.createCourse(courseMapper.coursePostDtoToCourse(requestBody));
        CourseResponseDto courseResponseDto = courseMapper.courseToCourseResponseDto(course);

        return new ResponseEntity<>(new SingleResponseDto<>(courseResponseDto), HttpStatus.OK);
    }

    @PatchMapping("/{courseId}")
    public ResponseEntity patchCourse(@Valid @RequestBody CoursePostDto requestBody,
                                      @PathVariable("courseId") Long courseId) {
        Course course = courseService.updateCourse(courseId, requestBody);
        CourseResponseDto courseResponseDto = courseMapper.courseToCourseResponseDto(course);

        return new ResponseEntity<>(new SingleResponseDto<>(courseResponseDto), HttpStatus.OK);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity getCourse(@PathVariable("courseId") Long courseId) {
        CourseResponseDto courseResponseDto = courseService.findCourse(courseId);
        return new ResponseEntity<>(new SingleResponseDto<>(courseResponseDto), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCourses(@RequestParam("page") int page,
                                     @RequestParam("size") int size) {
        Page<Course> pageCourses = courseService.findCourses(page - 1, size);
        return new ResponseEntity<>(new MultiResponseDto<>(courseMapper.coursesToCourseResponseDtos(pageCourses.getContent()), pageCourses), HttpStatus.OK);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
