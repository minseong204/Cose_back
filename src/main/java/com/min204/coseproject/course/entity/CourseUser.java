package com.min204.coseproject.course.entity;
import javax.persistence.*;
import com.min204.coseproject.user.entity.User;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CourseUser")
public class CourseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseUserId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EditPermission editPermission;

    public enum EditPermission {
        READ,
        EDIT,
        ADMIN
    }
}