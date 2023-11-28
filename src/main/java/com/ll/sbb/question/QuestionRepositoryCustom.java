package com.ll.sbb.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepositoryCustom {
    Question findBycontent(String content);
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject,String Conetent);

    List<Question> findBySubjectLike(String subject);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAllByKeyword(@Param("kw") String kw,Pageable pageable);

}
