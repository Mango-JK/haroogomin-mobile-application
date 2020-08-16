package com.mango.harugomin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    private String nickname;

    @Column(name = "profile_image")
    private String profileImage;

    private int point;
}
