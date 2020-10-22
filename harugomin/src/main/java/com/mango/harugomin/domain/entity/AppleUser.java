package com.mango.harugomin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "apple_user")
public class AppleUser {

    @Id
    @Column(name = "user_code")
    private String userCode;

    @Column(name = "user_id")
    private Long userId;

}
