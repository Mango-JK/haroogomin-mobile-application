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

    Optional<Liker> findByComment_CommentIdAndUserId(Long commentId, Long userId);
}
