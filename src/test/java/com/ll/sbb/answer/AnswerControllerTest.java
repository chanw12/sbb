package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionService;
import com.ll.sbb.user.SiteUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AnswerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AnswerService answerService;

    @MockBean
    QuestionService questionService;

    @InjectMocks
    AnswerController answerController;

    @Test
    @DisplayName("답변을 생성하는 create요청에 대한 테스트")
    @WithMockUser(username = "chan", password = "1234", roles = "USER")
    void test1() throws Exception{
        Integer questionId = 1;
        String content = "This is an answer content";

        when(questionService.getQuestion(questionId)).thenReturn(new Question());
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/create/{id}",questionId).param("content",content).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",questionId)));

        verify(answerService,times(1)).create(any(Question.class),eq(content),any(SiteUser.class));




    }
}
