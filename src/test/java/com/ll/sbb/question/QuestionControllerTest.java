package com.ll.sbb.question;

import com.ll.sbb.answer.AnswerForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
//    @Test
//    void test11() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/question/list"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("question_list"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("questionList"));
//    }

//    @Test
//    void test12() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
//                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/list"));
//    }

    @Test
    void detail() throws Exception {
        Integer questionId = 1;
        Question mockQuestion = new Question();
        mockQuestion.setId(questionId);
        mockQuestion.setSubject("Test Subject");
        mockQuestion.setAnswerList(new ArrayList<>());
        // questionService의 getQuestion 메서드가 호출될 때 가상의 Question을 반환하도록 설정
        when(questionService.getQuestion(questionId)).thenReturn(mockQuestion);



        // MockMvc를 사용하여 GET 요청을 보냄
        mockMvc.perform(MockMvcRequestBuilders.get("/question/detail/{id}", questionId))
                .andExpect(MockMvcResultMatchers.status().isOk()) // 반환된 HTTP 상태 코드 확인
                .andExpect(MockMvcResultMatchers.view().name("question_detail")) // 반환된 뷰 이름 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("question")); // 모델에 question 속성이 있는지 확인

        // questionService의 getQuestion 메서드가 한 번 호출되었는지 확인
        verify(questionService, times(1)).getQuestion(questionId);
    }

    @Test
    @DisplayName("/question/create으로 get요청이 가면 question_form뷰 페이지 보여주기")
    void test() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/question/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_form"));

    }

    @Test
    @DisplayName("/question/create으로 post요청시 질문 등록 요청 처리")
    void test1() throws Exception{
        String subject = "This is question subject";
        String content = "This is question content";
        mockMvc.perform(MockMvcRequestBuilders.post("/question/create").param("subject",subject).param("content",content))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/list"));
        verify(questionService,times(1)).create(eq(subject),eq(content));
    }

    @Test
    @DisplayName("/question/create으로 post요청시 질문 등록 요청이 올바르지 않을경우")
    void test2() throws Exception{
        QuestionForm questionForm = new QuestionForm();
        questionForm.setSubject("Test Subject");

        mockMvc.perform(MockMvcRequestBuilders.post("/question/create").flashAttr("questionForm",questionForm))
                .andExpect(MockMvcResultMatchers.status().isOk()) // 반환된 HTTP 상태 코드 확인
                .andExpect(MockMvcResultMatchers.view().name("question_form"))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.model().errorCount(1));

    }


    @Test
    @DisplayName("list뷰로 페이징 전달하기")
    void test3() throws Exception {
        Pageable pageable = PageRequest.of(0,10);
        List<Question> questions = new ArrayList<>();
        PageImpl<Question> page = new PageImpl<>(questions, pageable, 1);
        when(questionService.getList(0)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/question/list").param("page","0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_list"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("paging"));


        verify(questionService, times(1)).getList(0);
    }

}