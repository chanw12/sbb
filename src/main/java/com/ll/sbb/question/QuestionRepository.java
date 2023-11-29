package com.ll.sbb.question;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question,Integer> ,QuestionRepositoryCustom {
}
