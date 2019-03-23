package com.securitybox.editokenizerRest;

public class TokenizerDocument {

    private final long id;
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