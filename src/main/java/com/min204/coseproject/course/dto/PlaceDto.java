package com.min204.coseproject.course.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDto {
    private String address;
    private String placeName;
    private String placeUrl;
    private String categoryGroupName;
    private double x;
    private double y;
    private int placeOrder;
}