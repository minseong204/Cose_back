package com.min204.coseproject.course.entity;
import com.min204.coseproject.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.min204.coseproject.place.entity.Place;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "courses")
public class Course extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Place> places = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CourseUser> courseUsers = new HashSet<>();

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "preview_image_path", nullable = false)
    private String previewImagePath;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    public void addPlace(Place place) {
        this.places.add(place);
        place.setCourse(this);
        place.setPlaceOrder(this.places.size());
    }

    public void addCourseUser(CourseUser courseUser) {
        if (courseUsers == null) {
            courseUsers = new HashSet<>();
        }
        if (courseUsers.add(courseUser)) {
            courseUser.setCourse(this);
        }
    }
}
