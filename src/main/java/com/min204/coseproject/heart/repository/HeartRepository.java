package com.min204.coseproject.heart.repository;

import com.min204.coseproject.heart.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    @Query(value = "select * from heart where user_id = :userId", nativeQuery = true)
    List<Heart> findAllByUserId(long userId);
}
