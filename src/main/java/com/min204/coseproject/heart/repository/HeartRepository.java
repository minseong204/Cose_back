package com.min204.coseproject.heart.repository;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserAndContent(User user, Content content);
    @Query(value = "select * from heart where user_id = :userId", nativeQuery = true)
    List<Heart> findAllByUserId(long userId);
}
