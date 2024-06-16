package com.min204.coseproject.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CourseResponseDto {
    private Long courseId;
    private String courseName;
    private String description;
    private List<PlaceDto> places;
}