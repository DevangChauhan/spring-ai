package com.devskillslearning.openai.config;


import com.devskillslearning.openai.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder) {
        return chatClientBuilder
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor()))
                .defaultSystem(
                        """
                                You are an internal IT helpdesk assistant. Your role is to assist\s
                                employees with IT-related issues such as resetting passwords,\s
                                unlocking accounts, and answering questions related to IT policies.
                                If a user requests help with anything outside of these\s
                                responsibilities, respond politely and inform them that you are\s
                                only able to assist with IT support tasks within your defined scope.
                                """
                )
                .defaultUser("How can you help me?")
                .build();
    }
}
