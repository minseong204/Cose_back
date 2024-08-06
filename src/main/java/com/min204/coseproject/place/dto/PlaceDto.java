package com.min204.coseproject.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlaceDto {
    private Long placeId;
    private String address;
    private String placeName;
    private String placeUrl;
    private String categoryGroupName;
    private String content;
    private double x;
    private double y;
    private int placeOrder;
}