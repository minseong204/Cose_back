package com.min204.coseproject.follow.controller;

import com.min204.coseproject.constant.SuccessCode;
import com.min204.coseproject.follow.dto.FollowDto;
import com.min204.coseproject.follow.service.FollowService;
import com.min204.coseproject.response.CoseResponse;
import com.min204.coseproject.response.ResBodyModel;
import com.min204.coseproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userEmail = authentication.getName();
            Long userId = userService.getUserIdByEmail(userEmail);
            return userId;
        }
        throw new IllegalStateException("User is not authenticated");
    }

    @PostMapping("/{followeeId}")
    public ResponseEntity<ResBodyModel> followUser(@PathVariable Long followeeId) {
        Long followerId = getCurrentUserId();
        log.info("*************************followerId: {}", followerId);
        followService.followUser(followerId, followeeId);
        return CoseResponse.toResponse(SuccessCode.SUCCESS);
    }

    @DeleteMapping("/{followeeId}")
    public ResponseEntity<ResBodyModel> unfollowUser(@PathVariable Long followeeId) {
        Long followerId = getCurrentUserId();
        followService.unfollowUser(followerId, followeeId);
        return CoseResponse.toResponse(SuccessCode.SUCCESS);
    }

    @GetMapping("/followees")
    public ResponseEntity<List<FollowDto>> getFollowees() {
        Long userId = getCurrentUserId();
        log.info("Fetching followees for userId: {}", userId);
        List<FollowDto> followees = followService.getFollowees(userId);
        log.info("Found followees: {}", followees.size());
        return ResponseEntity.ok(followees);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowDto>> getFollowers() {
        Long userId = getCurrentUserId();
        log.info("Fetching followers for userId: {}", userId);
        List<FollowDto> followers = followService.getFollowers(userId);
        log.info("Found followers: {}", followers.size());
        return ResponseEntity.ok(followers);
    }
}