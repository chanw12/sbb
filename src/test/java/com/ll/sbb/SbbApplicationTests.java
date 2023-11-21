package com.ll.sbb;

import com.ll.sbb.answer.Answer;
import com.ll.sbb.answer.AnswerRepository;
import com.ll.sbb.question.Question;
import com.ll.sbb.question.QuestionRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class SbbApplicationTests {
    @Autowired
    EntityManager em;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;


    @Autowired
    private MockMvc mockMvc;




    @BeforeEach
    void setUp(){
        Question q1 = new Question();
        q1.setSubject("sbb가 무엇인가요?");
        q1.setContent("sbb에 대해서 알고 싶습니다.");
        q1.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q1);  // 첫번째 질문 저장

        Question q2 = new Question();
        q2.setSubject("스프링부트 모델 질문입니다.");
        q2.setContent("id는 자동으로 생성되나요?");
        q2.setCreateDate(LocalDateTime.now());
        this.questionRepository.save(q2);  // 두번째 질문

        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q2);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
        a.setCreateDate(LocalDateTime.now());
        this.answerRepository.save(a);
        System.out.println(q2.getAnswerList());

    }

    @Test
    void test1(){
        Question q3 = this.questionRepository.findBycontent("id는 자동으로 생성되나요?");
        // querydsl을 사용한 search 기능 사용
        Assertions.assertThat(q3.getContent()).isEqualTo("id는 자동으로 생성되나요?");
    }

    @Test
    void test2() {

        List<Question> all = this.questionRepository.findAll();
        Assertions.assertThat(2).isEqualTo(all.size());
        Question q = all.get(0);
        Assertions.assertThat(2).isEqualTo(all.size());

    }

    @Test
    void test3() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()) {
            Question q = oq.get();
            Assertions.assertThat("sbb가 무엇인가요?").isEqualTo(q.getSubject());
        }
    }

    @Test
    void test4() {
        Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");
        Assertions.assertThat("sbb가 무엇인가요?").isEqualTo(q.getSubject());
    }

    @Test
    void test5() {
        Question q = this.questionRepository.findBySubjectAndContent(
                "sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
        Assertions.assertThat("sbb가 무엇인가요?").isEqualTo(q.getSubject());
        Assertions.assertThat("sbb에 대해서 알고 싶습니다.").isEqualTo(q.getContent());
        Assertions.assertThat(1).isEqualTo(q.getId());


    }

    @Test
    void test6() { // like문
        List<Question> bySubjectLike = this.questionRepository.findBySubjectLike(
                "sbb%");
        Question q = bySubjectLike.get(0);
        Assertions.assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");


    }

    @Test
    void test7() {
        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()){
            Question q = oq.get();
            q.setSubject("수정된 제목");
            this.questionRepository.save(q);
        }

    }

    @Test
    void test8() {
        Assertions.assertThat(2).isEqualTo(this.questionRepository.count());

        Optional<Question> oq = this.questionRepository.findById(1);
        if(oq.isPresent()){
            Question q = oq.get();
            this.questionRepository.delete(q);

            Assertions.assertThat(1).isEqualTo(this.questionRepository.count());
        }

    }

    @Test
    void test9() {
        Optional<Question> oq = this.questionRepository.findById(2);
        if(oq.isPresent()){
            Question q = oq.get();
            Answer a = new Answer();
            a.setContent("네 자동으로 생성됩니다.");
            a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
            a.setCreateDate(LocalDateTime.now());
            this.answerRepository.save(a);

            Assertions.assertThat(2).isEqualTo(a.getQuestion().getId());
        }
    }
    @Test
    void test10() {
        Optional<Question> oq = this.questionRepository.findById(2);
        if(oq.isPresent()){
            Question q = oq.get();
            Answer a = new Answer();
            a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
            a.setContent("네 자동으로 생성됩니다.");
            a.setCreateDate(LocalDateTime.now());
            this.answerRepository.save(a);

            q.getAnswerList().size();


            List<Answer> answerList = q.getAnswerList();
            Assertions.assertThat(1).isEqualTo(answerList.size());
            Assertions.assertThat("네 자동으로 생성됩니다.").isEqualTo(answerList.get(0).getContent());
        }


    }

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




}
