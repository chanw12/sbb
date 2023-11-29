package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionRepository;
import com.ll.sbb.question.QuestionService;
import com.ll.sbb.user.SiteUser;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AnswerServiceTest {

    @InjectMocks
    AnswerService answerService;

    @Mock
    AnswerRepository answerRepository;


    @Test
    void create() {
        Question question = Question.builder().build();
        String answerContent = "This is an answer content";
        answerService.create(question, answerContent,new SiteUser());


        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

        verify(answerRepository, times(1)).save(answerCaptor.capture());

        Answer savedAnswer = answerCaptor.getValue();

        assertEquals(answerContent, savedAnswer.getContent());
        assertNotNull(savedAnswer.getCreateDate());
        assertEquals(question, savedAnswer.getQuestion());



    }
    @Test
    void getAnswer() {
       Answer answer= Answer.builder().id(1).build();
       when(answerRepository.findById(any(Integer.class))).thenReturn(Optional.of(answer));


        // Act
        Answer actualAnswer = answerService.getAnswer(1);

        // Assert
        assertEquals(answer, actualAnswer);
        verify(answerRepository, times(1)).findById(1);
        verifyNoMoreInteractions(answerRepository);


    }

    @Test
    void modify() {

        Answer answer = new Answer();
        String newContent = "Updated content";

        answerService.modify(answer, newContent);

        verify(answerRepository, times(1)).save(answer);
        assertNotNull(answer.getModifyDate());
        assertEquals(newContent, answer.getContent());
        verifyNoMoreInteractions(answerRepository);


    }

    @Test
    @DisplayName("답변 삭제 서비스 확인")
    void delete(){
        Answer answer= Answer.builder().id(1).build();


        // When
        doNothing().when(answerRepository).delete(answer);
        answerService.delete(answer);

        // Then
        verify(answerRepository, times(1)).delete(answer);
        verifyNoMoreInteractions(answerRepository);
    }


    @Test
    @DisplayName("추천 시스템")
    void vote(){

        Answer answer = new Answer();
        answer.setVoter(new HashSet<SiteUser>());
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        answer.getVoter().add(siteUser);


        ArgumentCaptor<Answer> argumentCaptor2 = ArgumentCaptor.forClass(Answer.class);

        answerService.vote(answer,siteUser);

        verify(answerRepository,times(1)).save(argumentCaptor2.capture());
        Answer value = argumentCaptor2.getValue();
        Assertions.assertThat(value.getVoter().stream().findFirst().get()).isEqualTo(siteUser);


    }

}