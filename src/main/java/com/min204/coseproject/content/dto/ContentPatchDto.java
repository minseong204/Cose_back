package com.min204.coseproject.content.dto;

import com.min204.coseproject.course.dto.CoursePostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContentPatchDto {
    private Long contentId;
    private String title;

    public void updateId(Long id) {
        this.contentId = id;
    }
}