package com.min204.coseproject.content.controller;

import com.min204.coseproject.content.dto.*;
import com.min204.coseproject.content.service.ContentService;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.constant.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @PostMapping
    public ResponseEntity<ResBodyModel> createContent(@Valid @RequestBody ContentPostDto contentPostDto) {
        contentService.createContent(contentPostDto);
        return CoseResponse.toResponse(SuccessCode.CONTENT_CREATED, contentPostDto, HttpStatus.CREATED.value());
    }

    @PatchMapping("/{contentId}")
    public ResponseEntity<ResBodyModel> updateContent(@PathVariable Long contentId,
                                                      @Valid @RequestBody ContentPatchDto contentPatchDto) {
        contentService.updateContent(contentId, contentPatchDto);
        return CoseResponse.toResponse(SuccessCode.CONTENT_UPDATED, contentPatchDto, HttpStatus.OK.value());
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ResBodyModel> getContent(@PathVariable Long contentId) {
        ContentResponseDto contentResponseDto = contentService.getContent(contentId);
        return CoseResponse.toResponse(SuccessCode.FETCH_SUCCESS, contentResponseDto, HttpStatus.PARTIAL_CONTENT.value());
    }

    @GetMapping
    public ResponseEntity<ResBodyModel> getAllContents() {
        List<ContentAllResponseDto> contents = contentService.getAllContents();
        return CoseResponse.toResponse(SuccessCode.FETCH_SUCCESS, contents, HttpStatus.PARTIAL_CONTENT.value());
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<ResBodyModel> deleteContent(@PathVariable Long contentId) {
        contentService.deleteContent(contentId);
        return CoseResponse.toResponse(SuccessCode.CONTENT_DELETED, contentId.toString(), HttpStatus.OK.value());
    }
}