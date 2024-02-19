package com.main.clouddefenceai.datamodels.response;

public enum SystemError {

    OK(200, "Success"),

    ACCESS_TOKEN_NOT_FOUND(400,"Access token Required."),
    
    USERNAME_TOKEN_NOT_FOUND(401,"Username required.");

    private Integer statusCode;

    private String statusMessage;

    SystemError(Integer statusCode, String statusMessage) {
	this.statusCode = statusCode;
	this.statusMessage = statusMessage;
    }

    public Integer getStatusCode() {
	return statusCode;
    }

    public String getStatusMessage() {
	return statusMessage;
    }

    public void setStatusCode(Integer statusCode) {
	this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
	this.statusMessage = statusMessage;
    }

}
