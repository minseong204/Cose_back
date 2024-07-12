package com.min204.coseproject.place.service;

import com.min204.coseproject.place.dto.PlaceDto;

public interface PlaceService {
    PlaceDto updatePlaceContent(Long placeId, String content);
}
