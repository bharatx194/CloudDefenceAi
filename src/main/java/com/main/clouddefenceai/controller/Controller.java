package com.main.clouddefenceai.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.main.clouddefenceai.datamodels.request.ChatRequest;
import com.main.clouddefenceai.datamodels.request.GetDataRequest;
import com.main.clouddefenceai.datamodels.response.BaseResponse;
import com.main.clouddefenceai.datamodels.response.ChatResponse;
import com.main.clouddefenceai.datamodels.response.GetDataResponse;
import com.main.clouddefenceai.datamodels.response.SystemError;
import com.main.clouddefenceai.services.ChatService;
import com.main.clouddefenceai.services.GitHubRepoDependencyFinder;

@RestController
@RequestMapping("/clouddefenceai")
public class Controller {

    private GitHubRepoDependencyFinder gitHubRepoDependencyFinder;

    private ChatService chatService;

    public Controller(GitHubRepoDependencyFinder gitHubRepoDependencyFinder, ChatService chatService) {
	this.gitHubRepoDependencyFinder = gitHubRepoDependencyFinder;
	this.chatService = chatService;
    }

    @PostMapping("/repofinder/getDepencencies")
    public BaseResponse getDependencies(@RequestBody GetDataRequest getDataRequest) throws Exception {
	BaseResponse baseResponse = new BaseResponse(SystemError.OK);
	if (ObjectUtils.isEmpty(getDataRequest.getAccess_token())) {
	    baseResponse = new BaseResponse(SystemError.ACCESS_TOKEN_NOT_FOUND);
	} else if (ObjectUtils.isEmpty(getDataRequest.getUsername())) {
	    baseResponse = new BaseResponse(SystemError.USERNAME_TOKEN_NOT_FOUND);
	}
	Map<String, List<String>> dependencies = gitHubRepoDependencyFinder.getAllDependencies(getDataRequest);
	GetDataResponse getDataResponse = new GetDataResponse(baseResponse);
	getDataResponse.setDependencies(dependencies);
	return getDataResponse;
    }

    @PostMapping("/generalChat")
    public ChatResponse generalChat(@RequestBody ChatRequest chatRequest) throws Exception {
	String response = chatService.getChatResponse(chatRequest);
	ChatResponse chatResponse = ChatResponse.builder().chatResponse(response).build();
	return chatResponse;
    }

}
