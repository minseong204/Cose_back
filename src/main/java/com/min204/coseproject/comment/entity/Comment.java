package com.min204.coseproject.comment.entity;

import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "COMMENTS")
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String body;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "CONTENT_ID")
    private Content content;

    public Comment(String body) {
        this.body = body;
    }

    public void setUser(User user) {
        this.user = user;
        if (!this.user.getComments().contains(this)) {
            this.user.getComments().add(this);
        }
    }

    public void setContent(Content content) {
        this.content = content;
        if (!this.content.getComments().contains(this)) {
            this.content.getComments().add(this);
        }
    }
}
