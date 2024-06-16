package com.min204.coseproject.content.repository;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    int countByUser(User user);
    List<Content> findAllByUser(User user);
}