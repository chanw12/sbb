package com.ll.sbb.question;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    QuestionService questionService;





    @Test
    void test11() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/question/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("questionList"));
    }

    @Test
    void test12() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/list"));
    }

    @Test
    void detail() throws Exception {

        Question mockedQuestion = new Question();
        mockedQuestion.setId(1);
        mockedQuestion.setContent("Test content");
        mockedQuestion.setSubject("ds123");
        when( questionService.getQuestion(1)).thenReturn(mockedQuestion);

        mockMvc.perform(MockMvcRequestBuilders.get("/question/detail/{id}",1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_detail"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("question"));

    }

}