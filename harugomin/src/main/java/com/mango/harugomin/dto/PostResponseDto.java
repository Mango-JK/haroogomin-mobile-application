package com.mango.harugomin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private Long userId;
    @Lob
    private String title;
    @Lob
    private String content;
    private String tagName;
    private String postImage;
    private int hits;
    private int postLikes;
    private LocalDateTime createdDate;
}
