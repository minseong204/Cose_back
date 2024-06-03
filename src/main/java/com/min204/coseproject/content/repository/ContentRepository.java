package com.min204.coseproject.content.repository;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    @Query("SELECT c FROM Content c LEFT JOIN FETCH c.courses WHERE c.contentId = :contentId")
    Optional<Content> findByIdWithCourses(@Param("contentId") Long contentId);

    int countByUser(User user);

    List<Content> findAllByUser(User user);
}
