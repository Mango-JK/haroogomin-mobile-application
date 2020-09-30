package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "liker")
public class Liker {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "liker_id")
    private Long likerId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Liker(Long userId, Comment comment) {
        this.userId = userId;
        this.comment = comment;
    }
}
