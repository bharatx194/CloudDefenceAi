package com.main.clouddefenceai.datamodels.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetDataRequest {
    
    private String access_token;
    
    private String username;
    
    private String repoName;

}
