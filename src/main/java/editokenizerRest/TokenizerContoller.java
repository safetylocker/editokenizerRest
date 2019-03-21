package editokenizerRest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.securitybox.constants.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static editokenizerRest.TokenizerApplication.edifact;

@RestController
@EnableWebMvc
@RequestMapping("/tokenizer")
public class TokenizerContoller {

    private static final String template = "EDI Tokenizer, %s!";
    private final AtomicLong counter = new AtomicLong();
    private JSONArray elementsToTokenize;
    private ArrayList senderIdList;
    private ArrayList receiverIdList;
    private String msgBody;



    @PostMapping
    @RequestMapping("/edidoc")
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    public TokenizerDocument tokenize(@RequestParam(value="msgType") String msgType,
                                      @RequestParam(value="elementsToTokenize",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="senderIdList",required = false) ArrayList senderIdList,
                                      @RequestParam(value="receiverIdList",required = false) ArrayList receiverIdList,
                                      @RequestBody() String msgBody) {
        //map inbound reqest parameters to instance parameters
        this.elementsToTokenize = elementsToTokenize;
        this.senderIdList = senderIdList;
        this.receiverIdList = receiverIdList;
        this.msgBody = msgBody;

        //log for debugging...
        System.out.println("Receieved Message : " + this.msgBody);
        System.out.println("Receieved elemnts to be tokenized : " + this.elementsToTokenize);
        System.out.println("Receieved sender id list : " + this.senderIdList);
        System.out.println("Receieved receiver id list : " + this.receiverIdList);

        //call EDIFACT rokenizer service..
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, callEDIFACT(this.msgBody,this.elementsToTokenize,senderIdList,receiverIdList)));
    }

    private String callEDIFACT(String input, JSONArray elementsToTokenize, ArrayList senderIdList, ArrayList receiverIdList){
        String response="";
        try {
            response = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE,elementsToTokenize,input,senderIdList,receiverIdList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }


}