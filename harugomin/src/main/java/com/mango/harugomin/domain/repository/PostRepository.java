package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

    @Override
    Post save(Post post);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findAllByTagName(String tagName, Pageable pageable);

    @Query(nativeQuery = true, value = " select * from post where content like %?1% OR title like %?1% ",
    countQuery = "SELECT COUNT(p.post_id) FROM post p WHERE p.title LIKE %?1% OR p.content LIKE %?1% ")
    Page<Post> searchAllPosts(String keyword, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update post set hits = hits + 1 where post_id = ?1 ", nativeQuery = true)
    void postHits(Long postId);

    Page<Post> findAllByUserUserId(Long userId, Pageable pageable);

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from post where user_id = ?1 ", nativeQuery = true)
    void deleteAllByUserUserId(Long userId);

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SET foreign_key_checks = 0 ", nativeQuery = true)
    void foreignkeyOpen();

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "SET foreign_key_checks = 1 ", nativeQuery = true)
    void foreignkeyClose();
}
