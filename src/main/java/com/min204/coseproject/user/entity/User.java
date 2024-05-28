package com.min204.coseproject.user.entity;

import com.min204.coseproject.comment.entity.Comment;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.user.dto.req.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sign_up_date")
    private Date signUpDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reset_token")
    private String resetToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public void changeInfo(UserRequestDto userRequestDto) {
        this.email = userRequestDto.getEmail();
        this.nickname = userRequestDto.getNickname();
    }

    //유저가 삭제되면, 작성 글과 좋아요도 삭제됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Content> contents = new ArrayList<>();

    //유저가 삭제되면, 좋아요가 삭제됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    //유저가 삭제되면, 댓글도 삭제됨
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    public void addHeart(Heart heart) {
        hearts.add(heart);
    }

    @Override
    public String getPassword() {
        return password;
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