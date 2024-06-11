
package com.min204.coseproject.content.entity;

import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.user.entity.User;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "contents")
public class Content extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long contentId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int viewCount = 0;

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;


    public void addCourse(Course course) {
        courses.add(course);
        course.setContent(this);
    }
}
