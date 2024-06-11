package com.min204.coseproject.user.dao;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.user.repository.UserPhotoRepository;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDao {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    // 중복된 findByEmail 메서드 제거
    // public User findByEmail(String email);

    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User find(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void delete(String email) {
        userRepository.findByEmail(email).ifPresent(userRepository::delete);
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserPhoto saveUserPhoto(UserPhoto userPhoto) {
        return userPhotoRepository.save(userPhoto);
    }

    public List<UserPhoto> findUserPhotosByUser(User user) {
        return userPhotoRepository.findByUser(user);
    }

    public void deleteUserPhoto(UserPhoto userPhoto) {
        userPhotoRepository.delete(userPhoto);
    }
}