package com.min204.coseproject.user.entity;

import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.constant.LoginType;
import com.min204.coseproject.constant.UserStatus;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.heart.entity.Heart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Table(name = "USERS")
@Entity
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String phone;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String image;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVITY;

    @Column
    @Enumerated(value = EnumType.STRING)
    private LoginType loginType = LoginType.LOCAL;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Content> contents = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    public User(
            String email,
            String password,
            String nickname
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void addHeart(Heart heart) {
        hearts.add(heart);
    }
}
