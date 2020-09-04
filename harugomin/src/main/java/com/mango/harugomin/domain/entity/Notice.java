package com.mango.harugomin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "notice")
public class Notice {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User targetUserId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post targetPostId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "comment_id")
    private Comment targetCommentId;

    @Column(name = "message", nullable = false, length = 500)
    private String message;

    @Column(name = "return_url", nullable = false)
    private String returnUrl;

    @Column(name = "is_read")
    private int isRead;

    @Column(name = "created_date")
    private LocalDateTime createdDate;
}
