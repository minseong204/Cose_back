package com.min204.coseproject.course.repository;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(Long courseId);

    @Query(value = "select * from courses where content_id = :contentId", nativeQuery = true)
    List<Course> findAllByContentId(long contentId);

    List<Course> findAllByContent(Content content);
}
