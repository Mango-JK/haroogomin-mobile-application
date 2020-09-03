package com.mango.harugomin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "hashtag")
public class Hashtag {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "tag_name")
    private String tagName;

    @Column(name = "posting_count")
    private Long postingCount;

    @Column(name = "total_count")
    private Long totalCount;

    @Builder
    public Hashtag(String tagName, long postingCount, long totalCount){
        this.tagName = tagName;
        this.postingCount = postingCount;
        this.totalCount = totalCount;
    }
}
