package com.ll.sbb.user;

import com.ll.sbb.answer.Answer;
import com.ll.sbb.question.Question;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest

class UserServiceTest {
    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    void create() {
        userService.create("name","email","password");
        ArgumentCaptor<SiteUser> argumentCaptor = ArgumentCaptor.forClass(SiteUser.class);

        verify(userRepository,times(1)).save(argumentCaptor.capture());
        SiteUser user = argumentCaptor.getValue();
        Assertions.assertThat(user.getUsername()).isEqualTo("name");
        Assertions.assertThat(user.getEmail()).isEqualTo("email");

    }



}