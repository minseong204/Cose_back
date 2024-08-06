package com.min204.coseproject.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.min204.coseproject.constant.LoginType;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sign_up_date")
    private Date signUpDate;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<String> roles = new HashSet<>();

    @Column(name = "refresh_token")
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private UserPhoto userPhoto;

    public void setUserPhoto(UserPhoto userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void changeInfo(UserRequestDto userRequestDto) {
        this.email = userRequestDto.getEmail();
        this.nickname = userRequestDto.getNickname();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}