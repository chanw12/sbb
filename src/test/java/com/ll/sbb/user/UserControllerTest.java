package com.ll.sbb.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    @DisplayName("회원가입 페이지를 보여준다")
    void signup() throws Exception{
        mockMvc.perform(get("/user/signup"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("signup_form"))
                .andExpect(handler().handlerType(UserController.class))
                .andExpect(handler().methodName("signup"));
    }

    @Test
    @DisplayName("회원가입 요청보내기")
    @WithAnonymousUser
    void testSignup() throws Exception {
        mockMvc.perform(post("/user/signup")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // 이걸해야되네...
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password1", "password123")
                        .param("password2", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

    }
    @Test
    @DisplayName("회원가입시 필수 파라미터가 누락된경우")
    void testSignupValidationFailure() throws Exception {
        mockMvc.perform(post("/user/signup").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup_form"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("비밀번호와 비밀번호확인이 일치하지 않는경우")
    void testSignupPasswordMismatch() throws Exception {
        mockMvc.perform(post("/user/signup").with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "testuser")
                        .param("email", "test@example.com")
                        .param("password1", "password123")
                        .param("password2", "password456"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup_form"))
                .andExpect(model().attributeHasFieldErrorCode("userCreateForm", "password2", "passwordInCorrect"));
    }


}