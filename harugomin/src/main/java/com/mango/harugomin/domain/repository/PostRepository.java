package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
