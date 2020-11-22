package com.mango.harugomin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "token")
public class Token {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "jwt")
    private String jwt;
}
