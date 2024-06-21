package com.min204.coseproject.course.mapper;

import com.min204.coseproject.course.dto.CoursePostDto;
import com.min204.coseproject.course.dto.CourseResponseDto;
import com.min204.coseproject.course.dto.PlaceDto;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.course.entity.Place;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    default Course coursePostDtoToCourse(CoursePostDto requestBody) {
        Course course = new Course();
        course.setCourseName(requestBody.getCourseName());

        Set<Place> places = requestBody.getPlaces().stream()
                .map(placeDto -> {
                    Place place = placeDtoToPlace(placeDto);
                    place.setCourse(course);
                    return place;
                })
                .collect(Collectors.toSet());
        course.setPlaces(places);

        return course;
    }

    default CourseResponseDto courseToCourseResponseDto(Course course) {
        Set<PlaceDto> placeDtos = course.getPlaces().stream()
                .map(this::placeToPlaceDto)
                .collect(Collectors.toSet());

        return CourseResponseDto.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .places(new ArrayList<>(placeDtos))
                .build();
    }

    default Place placeDtoToPlace(PlaceDto placeDto) {
        Place place = new Place();
        place.setAddress(placeDto.getAddress());
        place.setPlaceName(placeDto.getPlaceName());
        place.setPlaceUrl(placeDto.getPlaceUrl());
        place.setCategoryGroupName(placeDto.getCategoryGroupName());
        place.setX(placeDto.getX());
        place.setY(placeDto.getY());
        return place;
    }

    default PlaceDto placeToPlaceDto(Place place) {
        return PlaceDto.builder()
                .address(place.getAddress())
                .placeName(place.getPlaceName())
                .placeUrl(place.getPlaceUrl())
                .categoryGroupName(place.getCategoryGroupName())
                .x(place.getX())
                .y(place.getY())
                .build();
    }

    default List<CourseResponseDto> coursesToCourseResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(this::courseToCourseResponseDto)
                .collect(Collectors.toList());
    }
}