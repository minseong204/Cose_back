package com.min204.coseproject.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class UserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_photo_id")
    private Long userPhotoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String filePath;

    private Long fileSize;

    @Builder
    public UserPhoto(String originFileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public UserPhoto(String filePath) {
        this.originFileName = "defaultImage";
        this.filePath = filePath;
        this.fileSize = 0L;
    }

    public void addUser(User user) {
        this.user = user;
    }
}