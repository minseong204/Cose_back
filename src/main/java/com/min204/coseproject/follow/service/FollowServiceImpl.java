package com.min204.coseproject.follow.service;

import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.follow.dto.FollowDto;
import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.follow.repository.FollowRepository;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ResponseEntity<ResBodyModel> followUser(Long followeeId) {
        User follower = getCurrentUser();
        User followee = userService.find(followeeId);

        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new BusinessLogicException(ErrorCode.ALREADY_FOLLOWING);
        }
        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();
        followRepository.save(follow);

        String text = follower.getNickname() + "님이 " + followee.getNickname() + "님을 팔로우 하였습니다. ";

        return CoseResponse.toResponse(SuccessCode.FOLLOW_SUCCESS, text, HttpStatus.OK.value());
    }

    @Override
    @Transactional
    public ResponseEntity<ResBodyModel> unfollowUser(Long followeeId) {
        User follower = getCurrentUser();
        User followee = userService.find(followeeId);

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);

        String text = follower.getNickname() + "님이 " + followee.getNickname() + "님을 언팔로우 하였습니다.";

        return CoseResponse.toResponse(SuccessCode.UNFOLLOW_SUCCESS, text, HttpStatus.OK.value());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowDto> getFollowers() {
        User currentUser = getCurrentUser();
        return followRepository.findByFollowee(currentUser).stream()
                .map(follow -> new FollowDto(follow.getFollower().getUserId(), follow.getFollower().getEmail(), follow.getFollower().getNickname()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowDto> getFollowees() {
        User currentUser = getCurrentUser();
        return followRepository.findByFollower(currentUser).stream()
                .map(follow -> new FollowDto(follow.getFollowee().getUserId(), follow.getFollowee().getEmail(), follow.getFollowee().getNickname()))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            Long userId = userService.getUserIdByEmail(userEmail);
            return userService.find(userId);
        }
        throw new IllegalStateException("User is not authenticated");
    }
}