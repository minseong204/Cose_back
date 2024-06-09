package com.min204.coseproject.oauth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class OAuthUserPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_user_photo_id")
    private Long oAuthUserPhotoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oauth_user_id")
    private OAuthUser oAuthUser;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String filePath;

    private Long fileSize;

    @Builder
    public OAuthUserPhoto(String originFileName, String filePath, Long fileSize) {
        this.originFileName = originFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public void addOAuthUser(OAuthUser oAuthUser) {
        this.oAuthUser = oAuthUser;
    }
}