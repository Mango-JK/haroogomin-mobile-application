package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserId(Long userId);

    Long countByNickname(String nickname);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set point = point + 1 where user_id = ?1 ", nativeQuery = true)
    int upOnePoint(long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set point = point - 3 where user_id = ?1 ", nativeQuery = true)
    int useThreePoint(Long userId);

}
