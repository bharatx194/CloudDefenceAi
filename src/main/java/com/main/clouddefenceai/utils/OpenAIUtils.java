package com.main.clouddefenceai.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.main.clouddefenceai.datamodels.request.Messages;
import com.main.clouddefenceai.datamodels.request.OpenAiRequest;
import com.main.clouddefenceai.datamodels.response.ChatGptResponse;

@Service
public class OpenAIUtils {

    //I have removed my apikey from the application properties, you can add yours to test methods
    
    @Value("${apiKey}")
    private String apiKey;

    private CloseableHttpClient httpClient = HttpClients.custom()
	    .setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(Constants.TIME_OUT_IN_MILLI_SECONDS)
		    .setSocketTimeout(Constants.TIME_OUT_IN_MILLI_SECONDS).build())
	    .build();

    public ChatGptResponse getChatGpt_4_Response(String content) throws Exception {

	HttpPost request = new HttpPost(ChatGptModels.ChatGptUrl.getUrl());
	request.setHeaders(getHeaders());
	List<Messages> messagesList = new ArrayList<>();
	Messages message = Messages.builder().role(Constants.defaultRole).content(content).build();
	messagesList.add(message);
	OpenAiRequest chatGptRequest = OpenAiRequest.builder().temperature(Constants.defaultTemperature)
		.messages(messagesList).build();
	chatGptRequest.setModel(ChatGptModels.CHAT_GPT_4_NEW);
	request.setEntity(new StringEntity(Constants.objectMapper.writeValueAsString(chatGptRequest)));
	HttpResponse response = httpClient.execute(request);
	String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
	ChatGptResponse chatGptResponse = Constants.objectMapper.readValue(responseBody, ChatGptResponse.class);
	return chatGptResponse;

    }

    public Header[] getHeaders() {
	Header[] headers = new Header[] { new BasicHeader("Authorization", "Bearer " + apiKey),
		new BasicHeader("Content-Type", "application/json") };
	return headers;
    }

}
