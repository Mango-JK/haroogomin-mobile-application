package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
