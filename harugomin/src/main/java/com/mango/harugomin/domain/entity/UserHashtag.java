package com.mango.harugomin.domain.entity;

import lombok.Getter;
import javax.persistence.*;

@Getter
@Entity
@Table(name = "user_hashtag")
public class UserHashtag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_hashtag_id")
    private long userHashtagId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

}
