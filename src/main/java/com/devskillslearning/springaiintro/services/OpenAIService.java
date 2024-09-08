package com.devskillslearning.springaiintro.services;

import com.devskillslearning.springaiintro.model.Answer;
import com.devskillslearning.springaiintro.model.Question;

public interface OpenAIService {

    String getAnswer(String question);

    Answer getAnswer(Question question);
}
