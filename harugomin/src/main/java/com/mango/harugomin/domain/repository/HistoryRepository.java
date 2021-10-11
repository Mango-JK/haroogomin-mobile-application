package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Page<History> findAllByUserUserId(Long userId, Pageable pageable);

	@Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "delete from history where user_id = ?1 ", nativeQuery = true)
    void deleteAllByUsers(long userId);
}
