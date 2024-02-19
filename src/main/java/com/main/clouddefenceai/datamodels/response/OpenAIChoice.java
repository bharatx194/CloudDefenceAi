package com.main.clouddefenceai.datamodels.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OpenAIChoice {

    @JsonProperty("message")
    private Message message;

}
