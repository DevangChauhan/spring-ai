package com.devskillslearning.springaiintro.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenAIServiceImplTest {

    @Autowired
    private OpenAIService openAIService;

    @Test
    void getAnswer() {
        final String question = "What is the capital of India?";
        final String answer = openAIService.getAnswer(question);
        System.out.println("Got the answer");
        System.out.println(answer);
    }
}