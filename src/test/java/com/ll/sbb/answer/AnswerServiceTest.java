package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionRepository;
import com.ll.sbb.question.QuestionService;
import com.ll.sbb.user.SiteUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AnswerServiceTest {

    @InjectMocks
    AnswerService answerService;

    @Mock
    AnswerRepository answerRepository;


    @Test
    void create() {
        Question question = new Question();
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
       Answer answer= new Answer();
       answer.setId(1);
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
        Answer answer = new Answer();
        answer.setId(1);

        // When
        doNothing().when(answerRepository).delete(answer);
        answerService.delete(answer);

        // Then
        verify(answerRepository, times(1)).delete(answer);
        verifyNoMoreInteractions(answerRepository);
    }


}