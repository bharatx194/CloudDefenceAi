package com.main.clouddefenceai.datamodels.response;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetDataResponse extends BaseResponse {

    public GetDataResponse(BaseResponse baseResponse) {
	super(baseResponse);
    }

    private Map<String, List<String>> dependencies;

}
