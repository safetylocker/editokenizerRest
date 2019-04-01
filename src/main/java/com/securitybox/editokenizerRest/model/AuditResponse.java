package com.securitybox.editokenizerRest.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.securitybox.storage.AccessEntry;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AuditResponse {

    @ApiModelProperty(notes = "Request ID")
    private final long id;
    @ApiModelProperty(notes = "Response message")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private final ArrayList<AccessEntry> Response;


    public AuditResponse(long id, ArrayList response) {
        this.id = id;
        this.Response = response;
    }

}