package com.main.clouddefenceai.datamodels.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    private Integer httpStatus;

    private String message;

    public BaseResponse(SystemError systemError) {
	super();
	this.httpStatus = systemError.getStatusCode();
	this.message = systemError.getStatusMessage();
    }
    
    public BaseResponse(BaseResponse baseResponse) {
	super();
	this.httpStatus = baseResponse.httpStatus;
	this.message = baseResponse.message;
    }
    
}
