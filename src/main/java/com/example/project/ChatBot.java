package com.example.project;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class ChatBot {
    private String answer;
    ChatBot(){}
    ChatBot(String message){
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .build();
         answer = model.generate(message);
    }

    public void setMessage(String message) {
        ChatLanguageModel model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .build();
        this.answer = model.generate(message);
    }
    public String getAnswer(){
        return this.answer;
    }
}
