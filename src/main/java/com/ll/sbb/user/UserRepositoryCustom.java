package com.ll.sbb.user;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<SiteUser> findByusername(String username);
}
