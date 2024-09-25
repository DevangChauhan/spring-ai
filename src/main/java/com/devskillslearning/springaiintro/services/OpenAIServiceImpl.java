package com.devskillslearning.springaiintro.services;

import com.devskillslearning.springaiintro.model.Answer;
import com.devskillslearning.springaiintro.model.GetCapitalRequest;
import com.devskillslearning.springaiintro.model.GetCapitalResponse;
import com.devskillslearning.springaiintro.model.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatModel chatModel;

    public OpenAIServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Value("classpath:templates/get-capital-prompt.st")
    private Resource getCapitalPrompt;

    @Value("classpath:templates/get-capital-with-info.st")
    private Resource getCapitalPromptWithInfo;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public Answer getCapitalWithInfo(GetCapitalRequest getCapitalRequest) {
        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptWithInfo);
        final Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        final ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }


    @Override
    public GetCapitalResponse getCapital(GetCapitalRequest getCapitalRequest) {
        BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        String format = converter.getFormat();

        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        final Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry(),
                "format", format));
        final ChatResponse response = chatModel.call(prompt);
        return converter.convert(response.getResult().getOutput().getContent());
    }

    @Override
    public Answer getAnswer(Question question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question.question());
        final Prompt prompt = promptTemplate.create();
        final ChatResponse response = chatModel.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public String getAnswer(String question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question);
        final Prompt prompt = promptTemplate.create();
        final ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getContent();
    }
}
