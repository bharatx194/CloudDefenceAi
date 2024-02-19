package com.main.clouddefenceai.services;

import java.util.List;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import com.main.clouddefenceai.datamodels.request.ChatRequest;
import com.main.clouddefenceai.datamodels.response.ChatGptResponse;
import com.main.clouddefenceai.utils.OpenAIUtils;

@Service
public class ChatService {

    private OpenAIUtils openAIUtils;

    private CacheService cacheService;

    private String CURRENTLY_LOGGED_IN_USER = "CurrentUser";
    
    //Using cache to store previous chats of the user, in a prodiction level application
    //We can use security tokens as keys to store previous chats of the user.

    public ChatService(OpenAIUtils openAIUtils, CacheService cacheService) {
	this.openAIUtils = openAIUtils;
	this.cacheService = cacheService;
    }

    public String getChatResponse(ChatRequest chatRequest) throws Exception {
	if (ObjectUtils.isEmpty(chatRequest.getIsContinued()) || BooleanUtils.isFalse(chatRequest.getIsContinued())) {
	    chatRequest.setIsContinued(Boolean.FALSE);
	}
	String previousChat = "";
	if (BooleanUtils.isTrue(chatRequest.getIsContinued())) {
	    List<String> prevChat = cacheService.getValue(CURRENTLY_LOGGED_IN_USER);
	    if (ObjectUtils.isNotEmpty(prevChat)) {
		previousChat += prevChat.toString();
		previousChat += "New Prompt - ";
	    }
	} else if (BooleanUtils.isFalse(chatRequest.getIsContinued())) {
	    cacheService.clearCache(CURRENTLY_LOGGED_IN_USER);
	}

	String prompt = previousChat + chatRequest.getPrompt();
	ChatGptResponse chatGptResponse = openAIUtils.getChatGpt_4_Response(prompt);
	String response = chatGptResponse.getChoicesList().get(0).getMessage().getContent().toString();

	if (BooleanUtils.isTrue(chatRequest.getIsContinued())) {
	    String newPrevChat = "Previous Prompt - " + chatRequest.getPrompt() + " Previous Response" + response;
	    cacheService.putValue(CURRENTLY_LOGGED_IN_USER, newPrevChat);
	}
	return response;
    }

}
