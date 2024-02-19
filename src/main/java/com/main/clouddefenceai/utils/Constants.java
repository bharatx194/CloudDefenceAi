package com.main.clouddefenceai.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants {

    public static final Double defaultTemperature = 0.1;

    public static final String defaultRole = "system";

    public static final Integer zero = 0;

    public static final Integer TIME_OUT_IN_SECONDS = 10000000;

    public static final Integer TIME_OUT_IN_MILLI_SECONDS = TIME_OUT_IN_SECONDS * 1000000;

    public static ObjectMapper objectMapper = new ObjectMapper()
	    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

}
