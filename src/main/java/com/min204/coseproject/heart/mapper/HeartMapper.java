package com.min204.coseproject.heart.mapper;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.heart.dto.HeartPatchDto;
import com.min204.coseproject.heart.dto.HeartResponseDto;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HeartMapper {

    Heart heartPatchDtoToEntity(HeartPatchDto requestBody);

    default HeartResponseDto heartToHeartResponseDto(Heart heart) {
        User user = heart.getUser();
        Content content = heart.getContent();

        return HeartResponseDto.builder()
                .userId(user.getUserId())
                .heartId(heart.getHeartId())
                .contentId(content.getContentId())
                .heartType(heart.getHeartType().toString())
                .build();
    }

    List<HeartResponseDto> heartsToHeartResponseDtos(List<Heart> heart);
}
