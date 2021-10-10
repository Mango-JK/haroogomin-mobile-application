package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByUserId(Long userId);

    Page<Comment> findAllByPostPostId(Long postId, Pageable pageable);

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update comment set comment_likes = comment_likes + ?2 where comment_id = ?1 ", nativeQuery = true)
    void likeUpdate(Long commentId, int value);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from comment where user_id = ?1 ", nativeQuery = true)
    void deleteAllByUserId(Long userId);

}
