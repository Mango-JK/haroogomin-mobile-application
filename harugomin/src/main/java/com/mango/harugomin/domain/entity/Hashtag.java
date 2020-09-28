package com.mango.harugomin.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "hashtag")
public class Hashtag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String tagName;

    @JsonBackReference
    @OneToMany(mappedBy = "hashtag", fetch = EAGER)
    private List<UserHashtag> userHashtags = new ArrayList<>();

    @Column(name = "posting_count")
    private Long postingCount;

    @Builder
    public Hashtag(String tagName, long postingCount) {
        this.tagName = tagName;
        this.postingCount = postingCount;
    }
}
