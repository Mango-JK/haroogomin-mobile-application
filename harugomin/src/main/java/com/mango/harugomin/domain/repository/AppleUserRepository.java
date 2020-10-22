package com.mango.harugomin.domain.repository;

import com.mango.harugomin.domain.entity.AppleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppleUserRepository extends JpaRepository<AppleUser, String> {

}
