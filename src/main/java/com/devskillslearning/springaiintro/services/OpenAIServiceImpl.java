package com.devskillslearning.springaiintro.services;

import com.devskillslearning.springaiintro.model.Answer;
import com.devskillslearning.springaiintro.model.GetCapitalRequest;
import com.devskillslearning.springaiintro.model.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final ChatClient chatClient;

    public OpenAIServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
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
        final ChatResponse response = chatClient.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }


    @Override
    public Answer getCapital(GetCapitalRequest getCapitalRequest) {
        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalPrompt);
        final Prompt prompt = promptTemplate.create(Map.of("stateOrCountry", getCapitalRequest.stateOrCountry()));
        final ChatResponse response = chatClient.call(prompt);

        String responseString;
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getResult().getOutput().getContent());
            responseString = jsonNode.get("answer").asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new Answer(responseString);
    }

    @Override
    public Answer getAnswer(Question question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question.question());
        final Prompt prompt = promptTemplate.create();
        final ChatResponse response = chatClient.call(prompt);

        return new Answer(response.getResult().getOutput().getContent());
    }

    @Override
    public String getAnswer(String question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question);
        final Prompt prompt = promptTemplate.create();
        final ChatResponse response = chatClient.call(prompt);

        return response.getResult().getOutput().getContent();
    }
}
