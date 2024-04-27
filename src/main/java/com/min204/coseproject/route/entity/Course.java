package com.min204.coseproject.route.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.content.entity.Content;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "COURSES")
public class Course extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_ID")
    private Long courseId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "CONTENT_ID")
    private Content content;

    @Column(nullable = false)
    private String body;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private double x;

    @Column(nullable = false)
    private double y;

    @Column(nullable = false)
    private String address;

    public Route(
            String body,
            String place,
            double x,
            double y,
            String address
    ) {
        this.body = body;
        this.place = place;
        this.x = x;
        this.y = y;
        this.address = address;
    }
}
