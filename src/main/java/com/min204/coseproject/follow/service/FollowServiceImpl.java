package com.min204.coseproject.follow.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.follow.dto.FollowDto;
import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.follow.mapper.FollowMapper;
import com.min204.coseproject.follow.repository.FollowRepository;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final FollowMapper followMapper;

    @Transactional
    public void followUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_FOLLOWING);
        }

        Follow follow = Follow.builder()
                .follower(follower)
                .followee(followee)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowees(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        List<Follow> followees = followRepository.findByFollower(user);
        followees.forEach(follow -> {
            follow.getFollowee().getEmail(); // 강제 초기화
            follow.getFollower().getEmail(); // 강제 초기화
        });
        return followees.stream()
                .map(followMapper::followToFollowDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowDto> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        List<Follow> followers = followRepository.findByFollowee(user);
        followers.forEach(follow -> {
            follow.getFollowee().getEmail(); // 강제 초기화
            follow.getFollower().getEmail(); // 강제 초기화
        });
        return followers.stream()
                .map(followMapper::followToFollowDto)
                .collect(Collectors.toList());
    }
}