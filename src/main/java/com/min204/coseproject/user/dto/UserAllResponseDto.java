package com.min204.coseproject.user.dto;

import com.min204.coseproject.comment.dto.UserCommentResponse;
import com.min204.coseproject.content.dto.UserContentResponseDto;
import com.min204.coseproject.heart.dto.HeartListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserAllResponseDto {
    private Long userId;
    private String email;
    private String nickname;
    private String password;
    private String image;

    // 유저가 작성한 컨텐츠
    private List<UserContentResponseDto> contents;

    // 유저가 작성한 댓글
    private List<UserCommentResponse> comments;

    // 유저가 좋아요한 컨텐츠
    private List<HeartListDto> hearts;

    // 가입한 날짜시간
    private LocalDateTime createdAt;

    // 수정한 날짜 시간
    private LocalDateTime modifiedAt;
}
