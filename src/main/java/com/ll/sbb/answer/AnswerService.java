package com.ll.sbb.answer;

import com.ll.sbb.global.exception.DataNotFoundException;
import com.ll.sbb.question.Question;
import com.ll.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;


    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setAuthor(author);
        this.answerRepository.save(answer);
        return answer;
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    @Transactional
    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
    }


    @Transactional
    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    @Transactional
    public void vote(Answer answer, SiteUser siteUser){
        answer.getVoter().add(siteUser);
    }
}

