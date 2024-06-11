package com.min204.coseproject.scrap.repository;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.scrap.entity.Scrap;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByUser(User user);
    Optional<Scrap> findByUserAndContent(User user, Content content);

}