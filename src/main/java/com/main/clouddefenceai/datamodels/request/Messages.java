package com.main.clouddefenceai.datamodels.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Messages {

    private String role;

    private String content;

}
