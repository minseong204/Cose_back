package com.min204.coseproject.course.controller;

import com.min204.coseproject.course.dto.CoursePatchDto;
import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.service.CourseService;
import com.min204.coseproject.response.MultiResponseDto;
import com.min204.coseproject.response.SingleResponseDto;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/contents")
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
    @PostMapping("/{contentId}/courses")
    public ResponseEntity postCourse(@PathVariable("contentId") Long contentId,
                                     @Valid @RequestBody CoursePostDto requestBody) {
        requestBody.updateContentId(contentId);
        Course course = courseService.createCourse(
                courseMapper.coursePostDtoToCourse(requestBody));

        CourseResponseDto courseResponseDto =
                courseMapper.courseToCourseResponseDto(course);

        return new ResponseEntity(
                new SingleResponseDto<>(courseResponseDto), HttpStatus.OK
        );
    }

    @PatchMapping("/{contentId}/courses/{courseId}")
    public ResponseEntity patchCourse(@Valid @RequestBody CoursePatchDto requestBody,
                                      @PathVariable("courseId") @Positive Long courseId) {
        Course course = courseService.updateCourse(
                courseId,
                courseMapper.coursePatchDtoToCourse(requestBody));

        course.setCourseId(courseId);
        CourseResponseDto courseResponseDto =
                courseMapper.courseToCourseResponseDto(course);

        return new ResponseEntity<>(courseResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{contentId}/courses/{courseId}")
    public ResponseEntity getCourse(@PathVariable("courseId") Long courseId) {
        Course course = courseService.findCourse(courseId);
        CourseResponseDto courseResponseDto =
                courseMapper.courseToCourseResponseDto(course);

        return new ResponseEntity<>(courseResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{contentId}/courses")
    public ResponseEntity getCourses(@Positive @RequestParam("page") int page,
                                     @Positive @RequestParam("size") int size) {
        Page<Course> pageCourses = courseService.findCourses(page - 1, size);
        List<Course> courses = pageCourses.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(courseMapper.courseToCourseResponseDtos(courses), pageCourses), HttpStatus.OK
        );
    }

    @DeleteMapping("/{contentId}/courses/{courseId}")
    public ResponseEntity deleteCourse(@PathVariable("contentId") @Positive Long contentId,
                                       @PathVariable("courseId") @Positive Long courseId) {
        courseService.deleteCourse(courseId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
