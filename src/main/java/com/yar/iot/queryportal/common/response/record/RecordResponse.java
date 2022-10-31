package com.yar.iot.queryportal.common.response.record;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The record response
 * */
@ApiModel(description = "Record Response")
public class RecordResponse extends HashMap<String, Object> implements Serializable {

    private static final long serialVersionUID = -5005069881113793828L;

    public RecordResponse(Map<? extends String, ?> m) {
        super(m);
    }

    public RecordResponse() {
        super(new HashMap<>());
    }

}
