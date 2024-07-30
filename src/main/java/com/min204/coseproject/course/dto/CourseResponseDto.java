package com.min204.coseproject.course.dto;

import com.min204.coseproject.place.dto.PlaceDto;
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
    private double x;
    private double y;
    private String previewImagePath;
    private List<PlaceDto> places;
    private List<CourseUserResponseDto> members;
}