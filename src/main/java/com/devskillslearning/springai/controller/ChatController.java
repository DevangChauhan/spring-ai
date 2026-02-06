package com.devskillslearning.springai.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Chat", description = "Endpoints for AI-powered chat interactions")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping("/chat")
    @Operation(
            summary = "Send a message to the AI",
            description = "Returns the AI response for the given message",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response"),
                    @ApiResponse(responseCode = "400", description = "Bad Request - Message parameter missing"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error - OpenAI API error or configuration issue")
            }
    )
    public String chat(
            @Parameter(description = "The message to send to the AI", required = true)
            @RequestParam("message") String message) {
        return chatClient
                .prompt()
                //.advisors(new TokenUsageAuditAdvisor())
                .system(
                        """
                                You are an internal IT helpdesk assistant. Your role is to assist
                                employees with IT-related issues such as resetting passwords,
                                unlocking accounts, and answering questions related to IT policies.
                                        If a user requests help with anything outside of these
                                responsibilities, respond politely and inform them that you are
                                only able to assist with IT support tasks within your defined scope.
                                """
                )
                .user(message)
                .call()
                .content();
    }
}
