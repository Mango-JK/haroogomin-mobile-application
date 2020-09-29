package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user_hashtag")
public class UserHashtag {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_hashtag_id")
    private long userHashtagId;

    @JsonBackReference
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public UserHashtag(User user, Hashtag hashtag) {
        this.user = user;
        this.hashtag = hashtag;
    }
}
