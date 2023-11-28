package com.ll.sbb.question;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class QuestionRepositoryImpl  implements QuestionRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    public QuestionRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Question findBycontent(String content) {
        QQuestion question = QQuestion.question;

        return jpaQueryFactory.selectFrom(question)
                .where(question.content.contains(content)).fetchOne();

    }



    @Override
    public Question findBySubject(String subject) {
        QQuestion question = QQuestion.question;
        return jpaQueryFactory.selectFrom(question)
                .where(question.subject.contains(subject)).fetchOne();
    }

    @Override
    public Question findBySubjectAndContent(String subject, String content) {
        QQuestion question = QQuestion.question;
        return jpaQueryFactory.selectFrom(question)
                .where(question.subject.contains(subject)
                        .and(question.content.contains(content))).fetchOne();
    }

    @Override
    public List<Question> findBySubjectLike(String subject) {
        QQuestion question = QQuestion.question;
        return jpaQueryFactory.selectFrom(question)
                .where(question.subject.like(subject))
                .fetch();

    }


    @Override
    public Page<Question> findAll(Pageable pageable) {
        QQuestion question = QQuestion.question;
        List<Question> questions = this.jpaQueryFactory
                .selectFrom(question)
                .offset(pageable.getOffset())
                .orderBy(question.createDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = this.jpaQueryFactory
                .selectFrom(question)
                .fetch().size();
        return new PageImpl<>(questions, pageable, totalCount);

    }

    @Override
    public Page<Question> findAllByKeyword(String kw, Pageable pageable) {
        QQuestion question = QQuestion.question;
        List<Question> questions = this.jpaQueryFactory
                .selectFrom(QQuestion.question)
                .where(
                        QQuestion.question.subject.like("%" + kw + "%")
                                .or(QQuestion.question.content.like("%" + kw + "%"))
                )
                .offset(pageable.getOffset())
                .orderBy(question.createDate.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = this.jpaQueryFactory
                .selectFrom(question)
                .where(
                        QQuestion.question.subject.like("%" + kw + "%")
                                .or(QQuestion.question.content.like("%" + kw + "%"))
                )
                .fetch().size();
        return new PageImpl<>(questions,pageable,totalCount);
    }
}
