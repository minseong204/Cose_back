package com.min204.coseproject.scrap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.min204.coseproject.audit.Auditable;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Scrap extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrap_id")
    private Long scrapId;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Scrap(User user, Content content) {
        this.user = user;
        this.content = content;
    }
}