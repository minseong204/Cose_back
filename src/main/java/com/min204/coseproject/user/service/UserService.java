package com.min204.coseproject.user.service;

import com.min204.coseproject.auth.utils.CustomAuthorityUtils;
import com.min204.coseproject.comment.repository.CommentRepository;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.heart.repository.HeartRepository;
import com.min204.coseproject.response.SingleResponseDto;
import com.min204.coseproject.user.dto.UserAllResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.mapper.UserMapper;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;

    private final UserMapper userMapper;
    private final HeartRepository heartRepository;
    private final ContentRepository contentRepository;
    private final CommentRepository commentRepository;
    private final CourseRepository courseRepository;

    public User createUser(User user) {
        verifyExistsEmail(user.getEmail());
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        List<String> roles = authorityUtils.createRoles(user.getEmail());
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public User updateUser(User user) {
        User findUser = findVerifiedUser(user.getUserId());

        if (getLoginMember().getUserId() != findUser.getUserId()) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }
        Optional.ofNullable(user.getNickname())
                .ifPresent(nickname -> findUser.setNickname(nickname));
        Optional.ofNullable(user.getPassword())
                .ifPresent(password -> findUser.setPassword(passwordEncoder.encode(password)));
        return userRepository.save(findUser);
    }

    public User findUser(long userId) {
        return findVerifiedUser(userId);
    }

    public Page<User> findMembers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size,
                Sort.by("userId").descending()));
    }

    public void deleteUser(long userId) {
        User findUser = findVerifiedUser(userId);
        if (getLoginMember().getUserId() != findUser.getUserId()) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }

        userRepository.delete(findUser);
    }

    private void verifyExistsEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
    }

    private User findVerifiedUser(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return findUser;
    }

    public boolean emailCheck(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return false;
        }
        return true;
    }

    public User getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
        }

        Optional<User> optionalUser = userRepository.findByEmail(authentication.getName());
        User user = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        return user;
    }

    @Transactional(readOnly = true)
    public ResponseEntity detail(User user) {
        UserAllResponseDto userAllResponseDto = userMapper.InfoResponse(user, contentRepository, commentRepository, heartRepository, courseRepository);
        return new ResponseEntity<>(
                new SingleResponseDto<>(userAllResponseDto), HttpStatus.OK
        );
    }
}
