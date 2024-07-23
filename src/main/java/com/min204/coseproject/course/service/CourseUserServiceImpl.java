package com.min204.coseproject.course.service;

import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.entity.CourseUser;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.course.repository.CourseRepository;
import com.min204.coseproject.course.repository.CourseUserRepository;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseUserResponseDto inviteUser(Long courseId, Long userId, CourseUser.EditPermission editPermission) {
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        CourseUser courseUser = new CourseUser();
        courseUser.setCourse(course);
        courseUser.setUser(user);
        courseUser.setEditPermission(editPermission);

        CourseUser savedMapping = courseUserRepository.save(courseUser);
        return mapToDto(savedMapping);
    }

    @Override
    @Transactional
    public CourseUserResponseDto updateUserAccess(Long courseId, Long userId, CourseUser.EditPermission editPermission) {
        CourseUser mapping = courseUserRepository.findByCourse_CourseIdAndUser_UserId(courseId, userId)
                .orElseThrow(() -> new RuntimeException("User-Course mapping not found"));

        mapping.setEditPermission(editPermission);
        CourseUser updatedMapping = courseUserRepository.save(mapping);
        return mapToDto(updatedMapping);
    }

    @Override
    @Transactional
    public void removeUserFromCourse(Long courseId, Long userId) {
        courseUserRepository.deleteByCourse_CourseIdAndUser_UserId(courseId, userId);
    }

    @Override
    public List<CourseUserResponseDto> getAllUsersInCourse(Long courseId) {
        List<CourseUser> mappings = courseUserRepository.findAllByCourse_CourseId(courseId);
        return mappings.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseUserResponseDto getMyInfoInCourse(Long courseId, Long userId) {
        CourseUser mapping = courseUserRepository.findByCourse_CourseIdAndUser_UserId(courseId, userId)
                .orElseThrow(() -> new RuntimeException("User-Course mapping not found"));
        return mapToDto(mapping);
    }

    private CourseUserResponseDto mapToDto(CourseUser mapping) {
        UserProfileResponseDto userDto = courseMapper.userToCourseUserResponseDto(mapping.getUser());

        return CourseUserResponseDto.builder()
                .courseUserId(mapping.getCourseUserId())
                .courseId(mapping.getCourse().getCourseId())
                .email(userDto.getEmail())
                .name(userDto.getNickname())
                .profileImagePath(userDto.getProfileImagePath())
                .editPermission(mapping.getEditPermission())
                .build();
    }
}