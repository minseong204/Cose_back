package com.min204.coseproject.course.mapper;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.course.dto.CoursePatchDto;
import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.entity.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "sursepring")
public interface CourseMapper {

    default Course coursePostDtoToCourse(CoursePostDto requestBody) {
        Content content = new Content();
        content.setContentId(requestBody.getContentId());

        Course course = new Course();

        course.setContent(content);
        course.setX(requestBody.getX());
        course.setY(requestBody.getY());
        course.setAddress(requestBody.getAddress());
        course.setBody(requestBody.getBody());
        course.setPlace(requestBody.getPlace());

        return course;
    }

    default Course coursePatchDtoToCourse(CoursePatchDto requestBody) {
        Course course = new Course();

        course.setCourseId(requestBody.getCourseId());
        course.setAddress(requestBody.getAddress());
        course.setBody(requestBody.getBody());
        course.setX(requestBody.getX());
        course.setY(requestBody.getY());

        return course;
    }

    default CourseResponseDto courseToCourseResponseDto(Course course) {
        return CourseResponseDto.builder()
                .contentId(course.getContent().getContentId())
                .courseId(course.getCourseId())
                .body(course.getBody())
                .x(course.getX())
                .y(course.getY())
                .address(course.getAddress())
                .place(course.getPlace())
                .build();
    }

    List<CourseResponseDto> courseToCourseResponseDtos(List<Course> courses);
}
