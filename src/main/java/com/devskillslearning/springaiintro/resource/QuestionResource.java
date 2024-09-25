package com.devskillslearning.springaiintro.resource;

import com.devskillslearning.springaiintro.model.Answer;
import com.devskillslearning.springaiintro.model.GetCapitalRequest;
import com.devskillslearning.springaiintro.model.GetCapitalResponse;
import com.devskillslearning.springaiintro.model.Question;
import com.devskillslearning.springaiintro.services.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionResource {

    private final OpenAIService openAIService;

    public QuestionResource(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/capitalWithInfo")
    public Answer getCapitalWithInfo(@RequestBody GetCapitalRequest getCapitalRequest) {
        return openAIService.getCapitalWithInfo(getCapitalRequest);
    }

    @PostMapping("/capital")
    public GetCapitalResponse getCapital(@RequestBody GetCapitalRequest getCapitalRequest) {
        return openAIService.getCapital(getCapitalRequest);
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return openAIService.getAnswer(question);
    }

}
