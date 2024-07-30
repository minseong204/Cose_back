package com.min204.coseproject.place.controller;


import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.place.dto.PlaceDto;
import com.min204.coseproject.place.service.PlaceServiceImpl;
import com.min204.coseproject.response.CoseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceServiceImpl placeService;
    @PatchMapping("/{placeId}")
    public ResponseEntity<?> patchPlaceContent(@RequestBody(required = false) String content,
                                         @PathVariable("placeId") Long placeId) {
        content = (content == null) ? "" : content;
        PlaceDto placeDto = placeService.updatePlaceContent(placeId, content);
        return CoseResponse.toResponse(SuccessCode.COURSE_UPDATED, placeDto, HttpStatus.OK.value());
    }
}
