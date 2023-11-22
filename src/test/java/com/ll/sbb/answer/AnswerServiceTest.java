package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionRepository;
import com.ll.sbb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

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
        answerService.create(question, answerContent);


        ArgumentCaptor<Answer> answerCaptor = ArgumentCaptor.forClass(Answer.class);

        verify(answerRepository, times(1)).save(answerCaptor.capture());

        Answer savedAnswer = answerCaptor.getValue();

        assertEquals(answerContent, savedAnswer.getContent());
        assertNotNull(savedAnswer.getCreateDate());
        assertEquals(question, savedAnswer.getQuestion());



    }
}