package com.mango.harugomin.domain.repository;


import com.mango.harugomin.domain.entity.Hashtag;
import com.mango.harugomin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query(value = "select * from hashtag where tag_name = ?1 ", nativeQuery = true)
    Hashtag findByTagName(String tagName);

    @Modifying(clearAutomatically = true)
    @Query(value = "update hashtag set posting_count = posting_count + 1, total_count = total_count + 1 where tag_id = ?1 ", nativeQuery = true)
    void countUp(long tagId);
}
