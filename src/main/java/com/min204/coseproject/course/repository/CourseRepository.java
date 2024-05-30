package com.min204.coseproject.course.repository;

import com.min204.coseproject.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByContent_ContentId(Long contentId);

    @Query("SELECT c FROM Course c JOIN FETCH c.places WHERE c.courseId = :courseId")
    Optional<Course> findCourseWithPlaces(@Param("courseId") Long courseId);
}