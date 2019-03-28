package com.securitybox.editokenizerRest;

import io.swagger.annotations.ApiModelProperty;

public class TokenizerDocument {

    @ApiModelProperty(notes = "Request ID")
    private final long id;
    @ApiModelProperty(notes = "Response tokenize/detokenize message")
    private final String Response;


    public TokenizerDocument(long id, String response) {
        this.id = id;
        this.Response = response;
    }

    public long getId() {
        return id;
    }

    public String getResponse() {
        return Response;
    }
}