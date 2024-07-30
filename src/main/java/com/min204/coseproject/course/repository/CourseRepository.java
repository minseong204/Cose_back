package com.min204.coseproject.course.repository;

import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.user.dto.res.UserProfileResponseDto;
import com.min204.coseproject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c JOIN FETCH c.places WHERE c.courseId = :courseId")
    Optional<Course> findCourseWithPlaces(@Param("courseId") Long courseId);

    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.places LEFT JOIN FETCH c.courseUsers WHERE c.courseId = :courseId")
    Optional<Course> findByIdWithPlacesAndUsers(@Param("courseId") Long courseId);

    @Modifying
    @Query("UPDATE Course c SET c.modifiedAt = :currentTime WHERE c.courseId = :courseId")
    void updateModifiedAt(@Param("courseId") Long courseId, @Param("currentTime") LocalDateTime currentTime);
}