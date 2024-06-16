package com.min204.coseproject.content.mapper;

import com.min204.coseproject.content.dto.*;
import com.min204.coseproject.content.entity.Content;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    Content contentPostDtoToContent(ContentPostDto contentPostDto);
    ContentResponseDto contentToContentResponseDto(Content content);
    ContentAllResponseDto contentToContentAllResponseDto(Content content);  // 추가된 부분
    List<ContentAllResponseDto> contentToContentAllResponseDtos(List<Content> contentList);

    @Mapping(target = "contentId", ignore = true)
    void updateContentFromDto(ContentPatchDto contentPatchDto, @MappingTarget Content content);
}