package com.main.clouddefenceai.datamodels.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ChatGptResponse {

    @JsonProperty("choices")
    private List<OpenAIChoice> choicesList;

}
