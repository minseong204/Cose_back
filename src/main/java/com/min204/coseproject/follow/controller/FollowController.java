package com.min204.coseproject.follow.controller;

import com.min204.coseproject.follow.dto.FollowDto;
import com.min204.coseproject.follow.service.FollowService;
import com.min204.coseproject.response.ResBodyModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{followeeId}")
    public ResponseEntity<?> followUser(@PathVariable Long followeeId) {
        return followService.followUser(followeeId);
    }

    @DeleteMapping("/{followeeId}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long followeeId) {
        return followService.unfollowUser(followeeId);
    }

    @GetMapping("/followers")
    public ResponseEntity<ResBodyModel> getFollowers() {
        List<FollowDto> followers = followService.getFollowers();
        return ResponseEntity.ok(new ResBodyModel("Success", "200 OK", "팔로워 조회 성공", followers));
    }

    @GetMapping("/followees")
    public ResponseEntity<ResBodyModel> getFollowees() {
        List<FollowDto> followees = followService.getFollowees();
        return ResponseEntity.ok(new ResBodyModel("Success", "200 OK", "팔로잉 조회 성공", followees));
    }
}