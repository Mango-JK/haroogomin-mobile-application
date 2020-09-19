package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.UserHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHashtagRepository extends JpaRepository<UserHashtag, Long> {
}
