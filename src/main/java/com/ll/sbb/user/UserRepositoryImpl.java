package com.ll.sbb.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<SiteUser> findByusername(String username) {
        QSiteUser qSiteUser = QSiteUser.siteUser;


        return Optional.ofNullable(jpaQueryFactory.selectFrom(qSiteUser)
                .where(qSiteUser.username.eq(username))
                .fetchOne());
    }
}
