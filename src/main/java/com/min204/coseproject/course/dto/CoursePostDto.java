package com.min204.coseproject.course.dto;

import com.min204.coseproject.validator.NotSpace;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoursePostDto {
    private Long contentId;

    @NotSpace(message = "내용을 채워주세요")
    private String place;

    @NotSpace(message = "내용을 채워주세요")
    private String body;

    private double x;
    private double y;
    private String address;

    public void updateContentId(Long contentId) {
        this.contentId = contentId;
    }
}
