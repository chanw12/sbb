package com.ll.sbb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface UserRepository extends JpaRepository<SiteUser,Long>,UserRepositoryCustom {
}
