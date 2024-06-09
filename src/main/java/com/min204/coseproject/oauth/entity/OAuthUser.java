package com.min204.coseproject.oauth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oauth_user_id")
    private Long oAuthUserId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private OAuthProvider oAuthProvider;

    @OneToOne(mappedBy = "oAuthUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private OAuthUserPhoto oAuthUserPhoto;

    @Builder
    public OAuthUser(String email, String nickname, OAuthProvider oAuthProvider) {
        this.email = email;
        this.nickname = nickname;
        this.oAuthProvider = oAuthProvider;
    }

    public void setOAuthUserPhoto(OAuthUserPhoto oAuthUserPhoto) {
        this.oAuthUserPhoto = oAuthUserPhoto;
        oAuthUserPhoto.addOAuthUser(this);
    }

    public void getUser(OAuthUser oAuthUser) {

    }
}