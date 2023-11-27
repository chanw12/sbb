package com.ll.sbb.question;

import com.ll.sbb.answer.AnswerForm;
import com.ll.sbb.user.SiteUser;
import com.ll.sbb.user.UserService;
import org.apache.catalina.User;
import org.hamcrest.Matchers;
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
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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
    UserService userService;

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
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/question/create").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("chan").password("1234").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_form"));

    }

    @Test
    @DisplayName("/question/create으로 post요청시 질문 등록 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test1() throws Exception{
        String subject = "This is question subject";
        String content = "This is question content";
        mockMvc.perform(MockMvcRequestBuilders.post("/question/create").param("subject",subject).param("content",content)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/question/list"));
        verify(questionService,times(1)).create(eq(subject),eq(content),any(SiteUser.class));
    }

    @Test
    @DisplayName("/question/create으로 post요청시 질문 등록 요청이 올바르지 않을경우")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test2() throws Exception{
        QuestionForm questionForm = new QuestionForm();
        questionForm.setSubject("Test Subject");

        mockMvc.perform(MockMvcRequestBuilders.post("/question/create").flashAttr("questionForm",questionForm).with(SecurityMockMvcRequestPostProcessors.csrf()))
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

    @Test
    @DisplayName("modify뷰로 이동확인")
    void test4() throws Exception {
        Question question = new Question();

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        siteUser.setPassword("1234");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);

        mockMvc.perform(MockMvcRequestBuilders.get("/question/modify/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("chan").password("1234").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("question_form"));

    }

    @Test
    @DisplayName("정상적인 사용자가 아닐때  modify뷰로 이동시 badRequest 발생")
    void test5() throws Exception {

        Question question = new Question();

        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chanwoo");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);

        mockMvc.perform(MockMvcRequestBuilders.get("/question/modify/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.user("chan").password("1234").roles("USER")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());



    }

    @Test
    @DisplayName("/question/modify로 post요청시 질문 수정 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test6() throws Exception{
        String subject = "This is question modified subject";
        String content = "This is question modified content";
        Question question = new Question();
        question.setContent("this is question content");
        question.setSubject("this is question subject");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.post("/question/modify/{id}",1).param("subject",subject).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",1)));
        verify(questionService,times(1)).modify(eq(question),eq(subject),eq(content));

    }


    @Test
    @DisplayName("/question/modify로 post요청시 질문 수정 요청 처리 : 필수 요구 사항을 맞추지 못한경우")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test7() throws Exception{
        String subject = null;
        String content = "This is question modified content";
        Question question = new Question();
        question.setContent("this is question content");
        question.setSubject("this is question subject");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.post("/question/modify/{id}",1).param("subject",subject).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("question_form"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("questionForm","subject"))
                .andExpect(MockMvcResultMatchers.model().attribute("questionForm", Matchers.hasProperty("content", Matchers.is(content))));



    }

    @Test
    @DisplayName("/question/modify로 post요청시 질문 수정 요청 처리 : 수정 권한이 없는 유저인경우")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test8() throws Exception{
        String subject = "This is question modified subject";
        String content = "This is question modified content";
        Question question = new Question();
        question.setContent("this is question content");
        question.setSubject("this is question subject");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan123");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.post("/question/modify/{id}",1).param("subject",subject).param("content",content)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }


    @Test
    @DisplayName("/question/delete로 요청시 질문 삭제 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test9() throws Exception{
        Question question = new Question();
        question.setContent("this is question content");
        question.setSubject("this is question subject");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.get("/question/delete/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
        verify(questionService,times(1)).delete(eq(question));

    }

    @Test
    @DisplayName("/question/delete로 요청시 질문 삭제 요청 처리")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void test10() throws Exception{
        Question question = new Question();
        question.setContent("this is question content");
        question.setSubject("this is question subject");
        SiteUser siteUser = new SiteUser();
        siteUser.setUsername("chan1234");
        question.setAuthor(siteUser);
        when(questionService.getQuestion(1)).thenReturn(question);
        mockMvc.perform(MockMvcRequestBuilders.get("/question/delete/{id}",1)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("/question/vote/{id}로 요청시 추천 증가 ")
    @WithMockUser(username = "chan",password = "1234",roles = "USER")
    void vote() throws Exception{
        Question question = new Question();
        SiteUser siteUser = new SiteUser();
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("chan");
        Mockito.when(questionService.getQuestion(1)).thenReturn(question);
        Mockito.when(userService.getUser(principal.getName())).thenReturn(siteUser);
        mockMvc.perform(MockMvcRequestBuilders.post("/question/vote/{id}",1).principal(principal)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl(String.format("/question/detail/%s",1)));
        verify(questionService,times(1)).vote(question,siteUser);
    }

}