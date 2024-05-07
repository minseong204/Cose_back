package com.min204.coseproject.content.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.course.entity.Course;
import com.min204.coseproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "CONTENTS")
public class Content extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CONTENT_ID")
    private Long contentId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int viewCount = 0;

    @Column(nullable = false)
    private int heartCount = 0;

    @Column(nullable = false)
    private int amount = 0;

    @Column(nullable = false)
    private String travelDate;

    @OrderBy("heartId")
    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Course> courses = new ArrayList<>();

    @ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "USER_ID")
    private User user;

    public void addHeart(Heart heart) {
        hearts.add(heart);
    }
}
