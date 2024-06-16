package com.min204.coseproject.content.service;

import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.content.dto.*;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.mapper.ContentMapper;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;

    @Override
    public void createContent(ContentPostDto contentPostDto) {
        Content content = contentMapper.contentPostDtoToContent(contentPostDto);
        contentRepository.save(content);
    }

    @Override
    public void updateContent(Long contentId, ContentPatchDto contentPatchDto) {
        Content content = findVerifiedContent(contentId);
        contentMapper.updateContentFromDto(contentPatchDto, content);
        contentRepository.save(content);
    }

    @Override
    public ContentResponseDto getContent(Long contentId) {
        Content content = findVerifiedContent(contentId);
        return contentMapper.contentToContentResponseDto(content);
    }

    @Override
    public List<ContentAllResponseDto> getAllContents() {
        return contentRepository.findAll().stream()
                .map(contentMapper::contentToContentAllResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteContent(Long contentId) {
        Content content = findVerifiedContent(contentId);
        contentRepository.delete(content);
    }

    private Content findVerifiedContent(Long contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.CONTENT_NOT_FOUND));
    }
}