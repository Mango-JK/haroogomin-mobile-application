package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
