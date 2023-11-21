package com.ll.sbb.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    @GetMapping("/question/list")
    public String list(Model model){
        List<Question> questionList = this.questionService.getList();
        model.addAttribute("questionList",questionList);
        return "question_list";
    }

    @GetMapping("/")
    public String root(){
        return "redirect:/question/list";
    }

    @GetMapping(value ="/question/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id){
        Question question = questionService.getQuestion(id);
        model.addAttribute("question",question);

        return "question_detail";
    }
}
