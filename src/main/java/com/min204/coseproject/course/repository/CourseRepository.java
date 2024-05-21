package com.min204.coseproject.course.repository;

import com.min204.coseproject.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByContent_ContentId(Long contentId);
}