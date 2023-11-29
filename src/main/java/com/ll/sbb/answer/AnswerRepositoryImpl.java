package com.ll.sbb.answer;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerRepositoryImpl implements AnswerRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public AnswerRepositoryImpl(EntityManager em){
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Answer> findAll(Pageable pageable,int questionid) {
        QAnswer answer = QAnswer.answer;
        List<Answer> answers = jpaQueryFactory
                .selectFrom(answer)
                .where(answer.question.id.eq(questionid))
                .offset(pageable.getOffset())
                .orderBy(answer.createDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = this.jpaQueryFactory
                .selectFrom(answer)
                .where(answer.question.id.eq(questionid))
                .fetch().size();
    return new PageImpl<>(answers,pageable,totalCount);
    }
}
