package com.min204.coseproject.oauth.repository;

import com.min204.coseproject.oauth.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthUserRepository extends JpaRepository<OAuthUser, Long> {
    Optional<OAuthUser> findByEmail(String email);

    Boolean existsByEmail(String email);
}
