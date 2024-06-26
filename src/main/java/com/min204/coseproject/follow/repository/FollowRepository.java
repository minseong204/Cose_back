package com.min204.coseproject.follow.repository;

import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowee(User follower, User followee);
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowee(User followee);
    boolean existsByFollowerAndFollowee(User follower, User followee);

    int countByFollowee(User followee);

    int countByFollower(User follower);

    void deleteByFollowerUserIdOrFolloweeUserId(Long followerId, Long followeeId);

    // 추가된 부분
    @Query("SELECT f.follower FROM Follow f WHERE f.followee = :user")
    List<User> findFollowersByUser(@Param("user") User user);

    @Query("SELECT f.followee FROM Follow f WHERE f.follower = :user")
    List<User> findFolloweesByUser(@Param("user") User user);
}