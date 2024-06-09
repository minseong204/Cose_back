package com.min204.coseproject.user.mapper;

import com.min204.coseproject.user.dto.res.ResponseFollowerDto;
import com.min204.coseproject.user.dto.res.ResponseFolloweeDto;
import com.min204.coseproject.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FollowMapper {
    public ResponseFollowerDto toFollowerDto(User user) {
        return ResponseFollowerDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public ResponseFolloweeDto toFolloweeDto(User user) {
        return ResponseFolloweeDto.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    public List<ResponseFollowerDto> toFollowerDtoList(List<Object> users) {
        return users.stream().map(this::toFollowerDto).collect(Collectors.toList());
    }

    public List<ResponseFolloweeDto> toFolloweeDtoList(List<Object> users) {
        return users.stream().map(this::toFolloweeDto).collect(Collectors.toList());
    }
}
