package com.min204.coseproject.course.dto;

import com.min204.coseproject.validator.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoursePatchDto {
    private Long contentId;
    private Long courseId;

    @NotSpace(message = "내용을 채워주세요")
    private String place;

    @NotSpace(message = "내용을 채워주세요")
    private String body;

    private double x;

    private double y;

    private String address;


}
