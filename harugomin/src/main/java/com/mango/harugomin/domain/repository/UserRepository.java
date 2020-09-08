package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long id);

    @Modifying(clearAutomatically = true)
    @Query(nativeQuery = true, value = "update user set tag_id = ?2 where user_id = ?1 ")
    int updateUserHashTag(long userId, long tagId);

}
