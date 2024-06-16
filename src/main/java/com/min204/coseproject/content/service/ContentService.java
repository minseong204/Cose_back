package com.min204.coseproject.content.service;

import com.min204.coseproject.content.dto.*;
import com.min204.coseproject.content.entity.Content;

import java.util.List;

public interface ContentService {
    void createContent(ContentPostDto contentPostDto);
    void updateContent(Long contentId, ContentPatchDto contentPatchDto);
    ContentResponseDto getContent(Long contentId);
    List<ContentAllResponseDto> getAllContents();
    void deleteContent(Long contentId);
}