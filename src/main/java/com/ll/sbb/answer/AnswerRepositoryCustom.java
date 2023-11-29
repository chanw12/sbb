package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerRepositoryCustom {
    Page<Answer> findAll(Pageable pageable);
}
