package com.ll.sbb.answer;

import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionService;
import com.ll.sbb.user.SiteUser;
import com.ll.sbb.user.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AnswerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    AnswerService answerService;

    @MockBean
    QuestionService questionService;

    @MockBean
    UserService userService;


    @InjectMocks
    AnswerController answerController;

    @Test
    @DisplayName("답변을 생성하는 create요청에 대한 테스트")
    @WithMockUser(username = "chan", password = "1234", roles = "USER")
    void test1() throws Exception{
        Integer questionId = 1;
        Question question= new Question();
        question.setId(questionId);
        SiteUser siteUser = new SiteUser();
        String content = "This is an answer content";
        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("chan");
        when(userService.getUser(principal.getName())).thenReturn(siteUser);
        when(questionService.getQuestion(questionId)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/create/{id}",questionId).param("content",content).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",questionId)));

        verify(answerService,times(1)).create(any(Question.class),eq(content),any(SiteUser.class));




    }



    @Test
    @DisplayName("/answer/modify로 post요청시 질문 수정 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test6() throws Exception{
        String content = "This is answer modified content";
        Answer answer = new Answer();
        answer.setContent("this is answer content");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        answer.setAuthor(siteUser);
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);
        when(answerService.getAnswer(1)).thenReturn(answer);
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/modify/{id}",1).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",answer.getQuestion().getId())));
        verify(answerService,times(1)).modify(eq(answer),eq(content));

    }


    @Test
    @DisplayName("/answer/modify로 post요청시 질문 수정 요청 처리 : 필수 요구 사항을 맞추지 못한경우")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test7() throws Exception{
        String content = null;
        Answer answer = new Answer();
        answer.setContent("this is answer content");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        answer.setAuthor(siteUser);
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);
        when(answerService.getAnswer(1)).thenReturn(answer);
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/modify/{id}",1).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("answer_form"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("answerForm","content"));


    }

    @Test
    @DisplayName("/question/modify로 post요청시 질문 수정 요청 처리 : 수정 권한이 없는 유저인경우")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test8() throws Exception{
        String content = "This is answer modified content";
        Answer answer = new Answer();
        answer.setContent("this is answer content");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan123");
        answer.setAuthor(siteUser);
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);
        when(answerService.getAnswer(1)).thenReturn(answer);
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/modify/{id}",1).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("/answer/delete로 요청시 질문 삭제 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test9() throws Exception{
        Answer answer = new Answer();
        answer.setContent("this is question content");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        answer.setAuthor(siteUser);
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);
        when(answerService.getAnswer(1)).thenReturn(answer);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/delete/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",answer.getQuestion().getId())));
        verify(answerService,times(1)).delete(eq(answer));

    }

    @Test
    @DisplayName("/answer/delete로 요청시 질문 삭제 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test10() throws Exception{
        Answer answer = new Answer();
        answer.setContent("this is question content");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan123");
        answer.setAuthor(siteUser);
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);
        when(answerService.getAnswer(1)).thenReturn(answer);
        mockMvc.perform(MockMvcRequestBuilders.get("/answer/delete/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("/answer/vote/{id}로 요청시 추천 증가 ")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void vote() throws Exception{
        Answer answer = new Answer();
        Question question = new Question();
        question.setId(1);
        answer.setQuestion(question);

        SiteUser siteUser = new SiteUser();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("chan");
        Mockito.when(answerService.getAnswer(1)).thenReturn(answer);
        Mockito.when(userService.getUser(principal.getName())).thenReturn(siteUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/answer/vote/{id}",1).principal(principal)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",answer.getQuestion().getId())));
        verify(answerService,times(1)).vote(answer,siteUser);
    }
}
