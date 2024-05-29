package com.min204.coseproject.follow.service;

import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.follow.repository.FollowRepository;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public void followUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        User followee = userRepository.findById(followeeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 이미 팔로우 관계가 있는지 확인
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_FOLLOWING);
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        followRepository.save(follow);
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        User follower = userRepository.findById(followerId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        User followee = userRepository.findById(followeeId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new RuntimeException("팔로우 관계를 찾을 수 없음"));
        followRepository.delete(follow);
    }

    public List<User> getFollowees(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        List<Follow> followees = followRepository.findByFollower(user);

        return followees.stream()
                .map(Follow::getFollowee)
                .collect(Collectors.toList());
    }

    public List<User> getFollowers(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        List<Follow> followers = followRepository.findByFollowee(user);

        return followers.stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }
}
