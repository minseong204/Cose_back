package com.min204.coseproject.course.mapper;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CoursePreviewDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.dto.CourseUserResponseDto;
import com.min204.coseproject.course.entity.CourseUser;
import com.min204.coseproject.place.dto.PlaceDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.place.entity.Place;

import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    default Course coursePostDtoToCourse(CoursePostDto requestBody) {
        Course course = new Course();
        course.setCourseName(requestBody.getCourseName());
        course.setX(requestBody.getX());
        course.setY(requestBody.getY());
        course.setPreviewImagePath(requestBody.getPreviewImagePath());

        List<Place> places = requestBody.getPlaces().stream()
                .map(this::placeDtoToPlace)
                .collect(Collectors.toList());

        for (int i = 0; i < places.size(); i++) {
            places.get(i).setPlaceOrder(i + 1);
            places.get(i).setCourse(course);
        }
        course.setPlaces(places);

        return course;
    }

    default CourseResponseDto courseToCourseResponseDto(Course course) {
        List<PlaceDto> placeDtos = course.getPlaces().stream()
                .map(this::placeToPlaceDto)
                .collect(Collectors.toList());

        List<CourseUserResponseDto> courseUserResponseDtos = course.getCourseUsers().stream()
                .map(this::courseUserToCourseUserResponseDto)
                .collect(Collectors.toList());

        return CourseResponseDto.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .x(course.getX())
                .y(course.getY())
                .previewImagePath(course.getPreviewImagePath())
                .places(placeDtos)
                .members(courseUserResponseDtos)
                .build();
    }

    default CoursePreviewDto courseToCoursePreviewResponseDto(Course course) {
        LocalDateTime lastUpdated = course.getModifiedAt();
        if (lastUpdated == null) {
            lastUpdated = course.getCreatedAt();
        }

        return CoursePreviewDto.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .previewImagePath(course.getPreviewImagePath())
                .lastUpdated(lastUpdated)
                .build();
    }

    default List<CourseResponseDto> coursesToCourseResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(this::courseToCourseResponseDto)
                .collect(Collectors.toList());
    }

    default List<CoursePreviewDto> coursesToCoursePreviewResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(this::courseToCoursePreviewResponseDto)
                .collect(Collectors.toList());
    }

    default Place placeDtoToPlace(PlaceDto placeDto) {
        Place place = new Place();
        place.setAddress(placeDto.getAddress());
        place.setPlaceName(placeDto.getPlaceName());
        place.setPlaceUrl(placeDto.getPlaceUrl());
        place.setCategoryGroupName(placeDto.getCategoryGroupName());
        place.setContent(placeDto.getContent());
        place.setX(placeDto.getX());
        place.setY(placeDto.getY());
        return place;
    }

    default PlaceDto placeToPlaceDto(Place place) {
        return PlaceDto.builder()
                .placeId(place.getPlaceId())
                .address(place.getAddress())
                .placeName(place.getPlaceName())
                .placeUrl(place.getPlaceUrl())
                .categoryGroupName(place.getCategoryGroupName())
                .content(place.getContent())
                .x(place.getX())
                .y(place.getY())
                .placeOrder(place.getPlaceOrder())
                .build();
    }

    default UserProfileResponseDto userToUserProfileResponseDto(User user) {
        return UserProfileResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImagePath(user.getProfileImagePath())
                .build();
    }

    default CourseUserResponseDto courseUserToCourseUserResponseDto(CourseUser courseUser) {
        return CourseUserResponseDto.builder()
                .email(courseUser.getUser().getEmail())
                .nickname(courseUser.getUser().getNickname())
                .profileImagePath(courseUser.getUser().getProfileImagePath())
                .editPermission(courseUser.getEditPermission())
                .courseUserId(courseUser.getCourseUserId())
                .build();
    }
}
