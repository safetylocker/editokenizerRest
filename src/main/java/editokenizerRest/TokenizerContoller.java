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
    private JSONArray elementsToTokenize,elementsToDeTokenize;
    private ArrayList senderIdList;
    private ArrayList receiverIdList;
    private String msgBody,operation;




    @PostMapping
    @RequestMapping("/tokenize")
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    public TokenizerDocument tokenize(@RequestParam(value="msgType") String msgType,
                                      @RequestParam(value="elementsToTokenize",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="senderIdList",required = false) ArrayList senderIdList,
                                      @RequestParam(value="receiverIdList",required = false) ArrayList receiverIdList,
                                      @RequestParam(value="operation",required = true) String operation,
                                      @RequestBody() String msgBody) {
        //map inbound reqest parameters to instance parameters
        this.elementsToTokenize = elementsToTokenize;
        this.senderIdList = senderIdList;
        this.receiverIdList = receiverIdList;
        this.msgBody = msgBody;
        this.operation = operation;

        //log for debugging...
        System.out.println("Receieved Message : " + this.msgBody);
        System.out.println("Receieved elemnts to be tokenized : " + this.elementsToTokenize);
        System.out.println("Receieved sender id list : " + this.senderIdList);
        System.out.println("Receieved receiver id list : " + this.receiverIdList);

        //call EDIFACT rokenizer service..
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, EdifactTokenizer(this.msgBody,this.elementsToTokenize,senderIdList,receiverIdList,operation)));
    }


    @PostMapping
    @RequestMapping("/de-tokenize")
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    public TokenizerDocument detokenize(@RequestParam(value="msgType") String msgType,
                                      @RequestParam(value="elementsToDeTokenize",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="senderIdList",required = false) ArrayList senderIdList,
                                      @RequestParam(value="receiverIdList",required = false) ArrayList receiverIdList,
                                      @RequestParam(value="operation",required = true)String operation,
                                      @RequestBody() String msgBody) {
        //map inbound reqest parameters to instance parameters
        this.elementsToDeTokenize = elementsToDeTokenize;
        this.senderIdList = senderIdList;
        this.receiverIdList = receiverIdList;
        this.msgBody = msgBody;
        this.operation = operation;

        //log for debugging...
        System.out.println("Receieved Message : " + this.msgBody);
        System.out.println("Receieved elemnts to be de-tokenized : " + this.elementsToDeTokenize);
        System.out.println("Receieved sender id list : " + this.senderIdList);
        System.out.println("Receieved receiver id list : " + this.receiverIdList);

        //call EDIFACT rokenizer service..
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, EdifactTokenizer(this.msgBody,this.elementsToDeTokenize,senderIdList,receiverIdList,operation)));
    }

    private String EdifactTokenizer(String input, JSONArray elementsToDeTokenize, ArrayList senderIdList, ArrayList receiverIdList,String operation){
        String response="";
        try {
            if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_TOKENIZE))
                response = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE,elementsToDeTokenize,input,senderIdList,receiverIdList);
            else if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_DETOKENIZE))
                response = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_DETOKENIZE,elementsToDeTokenize,input,senderIdList,receiverIdList);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }



}