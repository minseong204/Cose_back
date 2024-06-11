package com.min204.coseproject.follow.entity;

import com.min204.coseproject.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followee_id")
    private User followee;

    // 추가: 접근자 메소드
    public Long getFollowerId() {
        return follower != null ? follower.getUserId() : null;
    }

    public String getFollowerEmail() {
        return follower != null ? follower.getEmail() : null;
    }

    public String getFollowerNickname() {
        return follower != null ? follower.getNickname() : null;
    }

    public Long getFolloweeId() {
        return followee != null ? followee.getUserId() : null;
    }

    public String getFolloweeEmail() {
        return followee != null ? followee.getEmail() : null;
    }

    public String getFolloweeNickname() {
        return followee != null ? followee.getNickname() : null;
    }
}