package com.main.clouddefenceai.utils;

public enum ChatGptModels {

    ChatGptUrl("https://api.openai.com/v1/chat/completions"),

    CHAT_REQUEST_COMPLETIONS("gpt-3.5-turbo"),

    CHAT_REQUEST_COMPLETIONS_TURBO("gpt-3.5-turbo-16k"),

    CHAT_MODERATION("text-moderation-001"),

    CHAT_EDIT("text-davinci-edit-001"),

    DAVINCI("text-davinci-001"),

    CHAT_GPT_4("gpt-4"),
    
    CHAT_GPT_4_NEW("gpt-4-1106-preview");
    
    private String chatGptModel;

    ChatGptModels(String chatGptModel) {
	this.chatGptModel = chatGptModel;
    }

    public String getUrl() {
	return this.chatGptModel;
    }

    public String getChatGptModel() {
	return this.chatGptModel;
    }

    public void setChatGptModel(String chatGptModel) {
	this.chatGptModel = chatGptModel;
    }

}
