package com.min204.coseproject.follow.repository;

import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowee(User followee);
    Optional<Follow> findByFollowerAndFollowee(User Follower, User Followee);
}
