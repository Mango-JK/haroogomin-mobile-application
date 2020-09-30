package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByTagName(String tagName, Pageable pageable);

    @Query(nativeQuery = true, value = " select * " +
            " from post " +
            " where content like %?1% " +
            " or title like %?1% ")
    Page<Post> searchAllPosts(String keyword, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "update post set hits = hits + 1 where post_id = ?1 ", nativeQuery = true)
    void postHits(Long postId);
}
