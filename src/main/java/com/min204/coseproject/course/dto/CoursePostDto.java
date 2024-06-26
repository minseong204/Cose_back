package com.min204.coseproject.course.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CoursePostDto {
    private String courseName;
    private List<PlaceDto> places;
}