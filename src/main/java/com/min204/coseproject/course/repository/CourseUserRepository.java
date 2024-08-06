package com.min204.coseproject.course.repository;

import com.min204.coseproject.course.entity.CourseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface CourseUserRepository extends JpaRepository<CourseUser, Long> {

    Optional<CourseUser> findByCourse_CourseIdAndUser_UserId(Long courseId, Long userId);

    void deleteByCourse_CourseIdAndUser_UserId(Long courseId, Long userId);

    List<CourseUser> findAllByCourse_CourseId(Long courseId);
}