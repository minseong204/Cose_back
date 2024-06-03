package com.min204.coseproject.course.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.content.entity.Content;
import javax.persistence.*;

import com.min204.coseproject.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "content_id")
    private Content content;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Place> places = new ArrayList<>();

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void addPlace(Place place) {
        this.places.add(place);
        place.setCourse(this);
    }
}