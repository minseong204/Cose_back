package com.min204.coseproject.scrap.mapper;

import com.min204.coseproject.content.dto.ContentDto;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.scrap.dto.ScrapDto;
import com.min204.coseproject.scrap.entity.Scrap;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrapMapper {

    default ScrapDto scrapToScrapDto(Scrap scrap) {
        return ScrapDto.builder()
                .scrapId(scrap.getScrapId())
                .content(contentToContentDto(scrap.getContent()))
                .build();
    }

    default ContentDto contentToContentDto(Content content) {
        return ContentDto.builder()
                .contentId(content.getContentId())
                .title(content.getTitle())
                .viewCount(content.getViewCount())
                .build();
    }
}