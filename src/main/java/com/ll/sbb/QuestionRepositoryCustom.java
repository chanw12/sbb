package com.ll.sbb;

import java.util.List;

public interface QuestionRepositoryCustom {
    Question findBycontent(String content);
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject,String Conetent);

    List<Question> findBySubjectLike(String subject);
}
