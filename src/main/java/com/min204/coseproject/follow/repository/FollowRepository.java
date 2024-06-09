package com.min204.coseproject.follow.repository;

import com.min204.coseproject.follow.entity.Follow;
import com.min204.coseproject.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower);
    List<Follow> findByFollowee(User followee);
    Optional<Follow> findByFollowerAndFollowee(User Follower, User Followee);

    int countByFollowee(User user);

    int countByFollower(User user);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followee.email = :email")
    int countByFolloweeEmail(@Param("email") String email);

    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.email = :email")
    int countByFollowerEmail(@Param("email") String email);
}
