package com.ll.sbb.question;

import com.ll.sbb.answer.AnswerForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
//    @GetMapping("/list")
//    public String list(Model model){
//        List<Question> questionList = this.questionService.getList();
//        model.addAttribute("questionList",questionList);
//        return "question_list";
//    }

    @GetMapping("/")
    public String root(){
        return "redirect:/question/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(AnswerForm answerForm,Model model, @PathVariable("id") Integer id ){
        Question question = questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @GetMapping("/create")
    public String create(QuestionForm questionForm){
        return "question_form";
    }

    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        questionService.create(questionForm.getSubject(),questionForm.getContent());
        return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
    }

    @GetMapping("/list")
    public String list(Model model,@RequestParam(value = "page",defaultValue = "0") int page){
        Page<Question> paging = this.questionService.getList(page);
        model.addAttribute("paging",paging);
        return "question_list";
    }

}
