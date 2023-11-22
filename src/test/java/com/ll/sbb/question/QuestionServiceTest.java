package com.ll.sbb.question;


import com.ll.sbb.global.exception.DataNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
class QuestionServiceTest {
    @InjectMocks
    QuestionService questionService;

    @Mock
    QuestionRepository questionRepository;


    @Test
    void test1(){
        //given
        Question question1 = new Question();
        question1.setId(1);
        question1.setContent("chan");
        question1.setSubject("Subject 1");
        question1.setCreateDate(LocalDateTime.now());

        Question question2 = new Question();
        question2.setId(1);
        question2.setContent("chan");
        question2.setSubject("Subject 2");
        question2.setCreateDate(LocalDateTime.now());
        when(questionRepository.findAll()).thenReturn(Arrays.asList(question1, question2));

        //when
        List<Question> result = questionService.getList();
        //then
        Assertions.assertThat(result).isEqualTo(questionRepository.findAll());
        Assertions.assertThat(2).isEqualTo(result.size()); // 예상되는 결과의 크기는 2여야 함
        Assertions.assertThat("Subject 1").isEqualTo(result.get(0).getSubject());
        Assertions.assertThat("Subject 2").isEqualTo(result.get(1).getSubject());


    }

    @Test
    void test2() {

        //given
        Question question1 = new Question();
        question1.setId(1);
        question1.setContent("chan");
        question1.setSubject("Subject 1");
        question1.setCreateDate(LocalDateTime.now());
        when(questionRepository.findById(1)).thenReturn(Optional.of(question1));

        //when
        Question question = questionService.getQuestion(1);

        //then

        Assertions.assertThat(question.getId()).isEqualTo(1);
        Assertions.assertThat(question.getSubject()).isEqualTo("Subject 1");

    }

    @Test
    void test3() {
        when(questionRepository.findById(any())).thenReturn(Optional.empty());
        org.junit.jupiter.api.Assertions.assertThrows(DataNotFoundException.class,()-> questionService.getQuestion(2));
        verify(questionRepository, times(1)).findById(eq(2));
    }

    @Test
    void test4(){
        String subject = "This is question subject";
        String content = "This is question content";

        questionService.create(subject,content);

        ArgumentCaptor<Question> argumentCaptor =  ArgumentCaptor.forClass(Question.class);

        verify(questionRepository,times(1)).save(argumentCaptor.capture());

        Question question = argumentCaptor.getValue();
        Assertions.assertThat(question.getSubject()).isEqualTo(subject);
        Assertions.assertThat(question.getContent()).isEqualTo(content);
        org.junit.jupiter.api.Assertions.assertNotNull(question.getCreateDate());

    }


}