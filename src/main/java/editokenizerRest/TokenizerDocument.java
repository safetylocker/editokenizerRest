package editokenizerRest;

public class TokenizerDocument {

    private final long id;
    private final String content;

    public TokenizerDocument(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}