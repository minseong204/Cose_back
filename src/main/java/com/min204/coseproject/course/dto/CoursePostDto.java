package com.min204.coseproject.course.dto;

import com.min204.coseproject.place.dto.PlaceDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CoursePostDto {
    private String courseName;
    private List<PlaceDto> places;


    /**
     * NOTE
     * 좌표 정보를 저장하게 될 경우 단 하나의 오차도 허용하지 않는 범위에서
     * double을 사용할 경우 1cm ~ 10cm의 오차범위가 발생할 수 있음.
     * bigdecimal 메서드를 valueOf를 통해서 사용하게 될경우 오차 범위를 허용하지 않음
     * -> 다만, bigdecimal의 경우 double 연산에 비해 매우 큰 오버헤드를 발생시킴
     */
    private double x;
    private double y;
    private String previewImagePath;
}