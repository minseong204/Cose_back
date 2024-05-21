package com.min204.coseproject.course.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDto {
    private String name;
    private double x;
    private double y;
    private String address;
    private String description;
}