package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    @Override
    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByTagName(String tagName, Pageable pageable);

    @Query(nativeQuery = true, value = " select * from post where content like %?1% OR title like %?1% ",
    countQuery = "SELECT COUNT(p.post_id) FROM post p WHERE p.title LIKE %?1% OR p.content LIKE %?1% ")
    Page<Post> searchAllPosts(String keyword, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Query(value = "update post set hits = hits + 1 where post_id = ?1 ", nativeQuery = true)
    void postHits(Long postId);

    Optional<List<Post>> findAllByUserUserId(Long userId);

}
