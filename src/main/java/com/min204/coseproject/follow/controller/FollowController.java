package com.min204.coseproject.follow.controller;

import com.min204.coseproject.follow.service.FollowService;
import com.min204.coseproject.user.dto.res.ResponseFollowerDto;
import com.min204.coseproject.user.dto.res.ResponseFolloweeDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.mapper.FollowMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final FollowMapper followMapper;

    @PostMapping("/{followerId}/follow/{followeeId}")
    public ResponseEntity<String> followUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        followService.followUser(followerId, followeeId);
        return new ResponseEntity<>("Follow successful", HttpStatus.OK);
    }

    @DeleteMapping("/{followerId}/unfollow/{followeeId}")
    public ResponseEntity<String> unfollowUser(@PathVariable Long followerId, @PathVariable Long followeeId) {
        followService.unfollowUser(followerId, followeeId);
        return new ResponseEntity<>("Unfollow successful", HttpStatus.OK);
    }

    @GetMapping("/{userId}/followees")
    public ResponseEntity<List<ResponseFolloweeDto>> getFollowees(@PathVariable Long userId) {
        List<User> followees = followService.getFollowees(userId);
        return new ResponseEntity<>(followMapper.toFolloweeDtoList(followees), HttpStatus.OK);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<ResponseFollowerDto>> getFollowers(@PathVariable Long userId) {
        List<User> followers = followService.getFollowers(userId);
        return new ResponseEntity<>(followMapper.toFollowerDtoList(followers), HttpStatus.OK);
    }
}
