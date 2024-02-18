package com.main.clouddefenceai.datamodels.request;

import java.util.List;

import com.main.clouddefenceai.utils.ChatGptModels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {

    private List<Messages> messages;

    private String model;

    private Double temperature;

    public void setModel(String model) {
	this.model = model;
    }

    public void setModel(ChatGptModels chatGptModel) {
	this.model = chatGptModel.getChatGptModel();
    }

}
