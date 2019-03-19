package editokenizerRest;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TokenizerContoller {

    private static final String template = "EDI Tokenizer, %s!";
    private final AtomicLong counter = new AtomicLong();
    private String msgBody;

    @RequestMapping("/tokenizer/edidoc")
    public TokenizerDocument greeting(@RequestParam(value="msgType") String msgType,
                                      @RequestBody() String msgBody) {
        this.msgBody = msgBody;
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, msgType + msgBody));
    }
}