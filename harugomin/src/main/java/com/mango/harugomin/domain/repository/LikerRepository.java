package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Liker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikerRepository extends JpaRepository<Liker, Long> {

    @Query(value = "select count(*) from liker where comment_id = ?1 AND user_id = ?2 ", nativeQuery = true)
    int findLiker(Long commentId, Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from liker l where l.comment_id IN (select c.comment_id from comment c where c.post_id = ?1) ", nativeQuery = true)
    int deleteAllByPostId(Long postId);

    Optional<Liker> findByComment_CommentIdAndUserId(Long commentId, Long userId);
}
