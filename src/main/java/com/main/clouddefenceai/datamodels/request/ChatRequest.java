package com.main.clouddefenceai.datamodels.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    
    private String prompt;

    private Boolean isContinued;
    
}
