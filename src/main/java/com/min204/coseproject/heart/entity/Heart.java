package com.min204.coseproject.heart.entity;

import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.constant.HeartType;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
public class Heart extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long heartId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private HeartType heartType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    public Heart(User user, Content content) {
        this.user = user;
        this.content = content;
    }

    public void addUser(User user) {
        this.user = user;
        user.addHeart(this);
    }

    public void addContent(Content content) {
        this.content = content;
        content.addHeart(this);
    }
}
