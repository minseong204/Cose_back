package com.min204.coseproject.follow.service;

import com.min204.coseproject.follow.dto.FollowDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FollowService {
    ResponseEntity<?> followUser(Long followeeId);
    ResponseEntity<?> unfollowUser(Long followeeId);
    List<FollowDto> getFollowers();
    List<FollowDto> getFollowees();
}