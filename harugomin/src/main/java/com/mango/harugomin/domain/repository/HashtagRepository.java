package com.mango.harugomin.domain.repository;


import com.mango.harugomin.domain.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Override
    Optional<Hashtag> findById(Long tagId);

    Hashtag findByTagName(String tagName);

    @Modifying(clearAutomatically = true)
    @Query(value = "update hashtag set posting_count = posting_count + 1 where tag_id = ?1 ", nativeQuery = true)
    void countUp(long tagId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user_hashtag where user_id = ?1 ", nativeQuery = true)
    void deleteByUserId(Long userId);

    Page<Hashtag> findAllTags(Pageable pageable);
}
