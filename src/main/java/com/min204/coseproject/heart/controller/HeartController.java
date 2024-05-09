package com.min204.coseproject.heart.controller;

import com.min204.coseproject.constant.HeartType;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.service.ContentService;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.heart.dto.HeartResponseDto;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.heart.mapper.HeartMapper;
import com.min204.coseproject.heart.repository.HeartRepository;
import com.min204.coseproject.heart.service.HeartService;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserServiceImpl;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class HeartController {
    private final HeartMapper heartMapper;
    private final HeartService heartService;
    private final UserServiceImpl userServiceImpl;
    private final ContentService contentService;
    private final HeartRepository heartRepository;

    @PostMapping("/{email}/{contentId}/hearts")
    public ResponseEntity postHeart(
            @PathVariable("email") @Positive String email,
            @PathVariable("contentId") @Positive Long contentId
    ) {
        User user = userServiceImpl.find(email);

        if (userServiceImpl.getLoginMember().getUserId() != user.getUserId()) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }

        Content content = contentService.findContent(contentId);
        Heart heart = heartService.createHeart(user, content);
        HeartResponseDto response = heartMapper.heartToHeartResponseDto(heart);

        if (heart.getHeartType() == HeartType.REMOVE) {
            heartRepository.delete(heart);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
}
