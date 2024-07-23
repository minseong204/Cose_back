package com.min204.coseproject.course.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CoursePreviewDto {
    private Long courseId;
    private String courseName;
    private String previewImagePath;
    private LocalDateTime lastUpdated;
}

