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
        course.setDescription(requestBody.getDescription());

        Set<Place> places = requestBody.getPlaces().stream()
                .map(placeDto -> {
                    Place place = placeDtoToPlace(placeDto);
                    place.setCourse(course); // 각 Place에 Course 설정
                    return place;
                })
                .collect(Collectors.toSet()); // Collect to Set
        course.setPlaces(places);

        return course;
    }

    default CourseResponseDto courseToCourseResponseDto(Course course) {
        Set<PlaceDto> placeDtos = course.getPlaces().stream()
                .map(this::placeToPlaceDto)
                .collect(Collectors.toSet());

        return CourseResponseDto.builder()
                .courseId(course.getCourseId())
                .description(course.getDescription())
                .places(new ArrayList<>(placeDtos)) // Convert Set to List
                .build();
    }

    default Place placeDtoToPlace(PlaceDto placeDto) {
        Place place = new Place();
        place.setName(placeDto.getName());
        place.setX(placeDto.getX());
        place.setY(placeDto.getY());
        place.setAddress(placeDto.getAddress());
        place.setDescription(placeDto.getDescription());
        return place;
    }

    default PlaceDto placeToPlaceDto(Place place) {
        return PlaceDto.builder()
                .name(place.getName())
                .x(place.getX())
                .y(place.getY())
                .address(place.getAddress())
                .description(place.getDescription())
                .build();
    }

    default List<CourseResponseDto> coursesToCourseResponseDtos(List<Course> courses) {
        return courses.stream()
                .map(this::courseToCourseResponseDto)
                .collect(Collectors.toList());
    }
}