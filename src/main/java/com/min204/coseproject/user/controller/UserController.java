package com.min204.coseproject.user.controller;

import com.min204.coseproject.user.dto.UserPatchDto;
import com.min204.coseproject.user.dto.UserPostDto;
import com.min204.coseproject.user.dto.UserResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.mapper.UserMapper;
import com.min204.coseproject.user.repository.UserRepository;
import com.min204.coseproject.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    //    @PostMapping
//    public ResponseEntity postUser(@Valid @RequestBody UserPostDto userPostDto) {
//        User user = userService.createUser(userMapper.userPostDtoToUser(userPostDto));
//        int i = (int) (Math.random() * (8) - 1 + 1);
//        user.setImage("https://{s3주소}" + i + ".png");
//        userRepository.save(user);
//        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);
//
//        return new ResponseEntity(userResponseDto, HttpStatus.CREATED);
//    }
    private static final String DIRECTORY = System.getProperty("../resources/images") + "/uploaded-images";

    // TODO : 사진 파일 업로드 방식 AWS S3로 바꿔야함
    @PostMapping
    public ResponseEntity<?> postUser(@Valid @RequestBody UserPostDto userPostDto, @RequestParam("image") MultipartFile file) throws Exception {
        User user = userService.createUser(userMapper.userPostDtoToUser(userPostDto));

        if (!file.isEmpty()) {
            userService.saveFile(DIRECTORY, file.getOriginalFilename(), file);
            user.setImage("/uploaded-images/" + file.getOriginalFilename());
        }

        userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity patchUser(@PathVariable("userId") @Positive Long userId,
                                    @Valid @RequestBody UserPatchDto userPatchDto) {
        userPatchDto.setUserId(userId);
        User user = userService.updateUser(userMapper.userPatchDtoToUser(userPatchDto));
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

//    @PatchMapping("/{userId}/image")
//    public ResponseEntity patchUser(@PathVariable("userId") @Positive Long userId,
//                                    @RequestPart(value = "imgFile", required = false) MultipartFile image) throws IOException {
//        User user = userService.findUser(userId);
//
//        String profileImage = s3Uploader.uploadRouteImages(image);
//        user.setImage(profileImage);
//        userRepository.save(user);
//        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);
//
//        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//    }

    // TODO : 사진 수정방식도 마찬가지로 변경해야함
    @PatchMapping("/{userId}/image")
    public ResponseEntity<?> patchUser(@PathVariable("userId") @Positive Long userId,
                                       @RequestPart(value = "imgFile", required = false) MultipartFile image) throws Exception {

        User user = userService.findUser(userId);

        if (image != null && !image.isEmpty()) {
            userService.saveFile(DIRECTORY, image.getOriginalFilename(), image);
            user.setImage("/uploaded-images/" + image.getOriginalFilename());
        }

        userRepository.save(user);
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity getUser(@PathVariable("userId") Long userId) {
        User user = userService.findUser(userId);
        UserResponseDto userResponseDto = userMapper.userToUserResponseDto(user);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}/info")
    public ResponseEntity getUserInfo(@PathVariable("userId") @Positive Long userId) {
        User user = userService.findUser(userId);

        return userService.detail(user);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUser(@PathVariable("userId") @Positive Long userId) {
        userService.deleteUser(userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/email-check/{email}")
    public ResponseEntity<Boolean> verifyExistsEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(userService.emailCheck(email));
    }
}
