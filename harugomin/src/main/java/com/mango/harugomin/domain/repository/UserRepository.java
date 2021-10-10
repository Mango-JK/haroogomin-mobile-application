package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Override
    Optional<User> findById(Long userId);

    Optional<User> findByUserLoginId(String id);

    Long countByNickname(String nickname);

    Long countByUserLoginId(String id);

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from user where user_id = ?1 ", nativeQuery = true)
    void deleteUser(Long userId);
}
