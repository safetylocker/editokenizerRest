package com.securitybox.editokenizerRest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.securitybox.constants.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.securitybox.editokenizerRest.TokenizerApplication.edifact;

@RestController
@EnableWebMvc
@RequestMapping("/tokenizer")
@Api(value="Tokenization REST API.", description="API operation supported for tokenization system")
public class TokenizerContoller {

    private static final String template = "%s!";
    private final AtomicLong counter = new AtomicLong();
    private JSONArray elementsToTokenize,elementsToDeTokenize;
    private ArrayList senderIdList;
    private ArrayList receiverIdList;
    private String document;





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

    @PostMapping
    @RequestMapping(value = "/tokenize", method = RequestMethod.POST)
    @ApiOperation(value = "Tokenize an electronic message")
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    public TokenizerDocument tokenize(@RequestParam(value="Elements to be Tokenized",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="Sender Id(S)",required = false) ArrayList senderIdList,
                                      @RequestParam(value="Receiver Id(s)",required = false) ArrayList receiverIdList,
                                      @RequestBody() String Document) {
        //map inbound reqest parameters to instance parameters
        this.elementsToTokenize = elementsToTokenize;
        this.senderIdList = senderIdList;
        this.receiverIdList = receiverIdList;
        this.document = Document;

        //log for debugging...
        System.out.println("Receieved Message : " + this.document);
        System.out.println("Receieved elemnts to be tokenized : " + this.elementsToTokenize);
        System.out.println("Receieved sender id list : " + this.senderIdList);
        System.out.println("Receieved receiver id list : " + this.receiverIdList);

        //call EDIFACT rokenizer service..
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, EdifactTokenizer(this.document,this.elementsToTokenize,senderIdList,receiverIdList,Constants.TOKENIZER_METHOD_TOKENIZE)));
    }


    @PostMapping
    @RequestMapping(value = "/de-tokenize",method = RequestMethod.POST)
    @ApiOperation(value = "De-Tokenize an electronic message")
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    public TokenizerDocument detokenize(
            @RequestParam(value="Elements to be de-tokenzied",required = true) JSONArray elementsToDeTokenize,
            @RequestParam(value="Sender Id(s)",required = false) ArrayList senderIdList,
            @RequestParam(value="Receiver Id(s)",required = false) ArrayList receiverIdList,
            @RequestBody(required = true) String Document) {
        //map inbound reqest parameters to instance parameters
        this.elementsToDeTokenize = elementsToDeTokenize;
        this.senderIdList = senderIdList;
        this.receiverIdList = receiverIdList;
        this.document = Document;

        //log for debugging...
        System.out.println("Receieved Message : " + this.document);
        System.out.println("Receieved elemnts to be de-tokenized : " + this.elementsToDeTokenize);
        System.out.println("Receieved sender id list : " + this.senderIdList);
        System.out.println("Receieved receiver id list : " + this.receiverIdList);

        //call EDIFACT rokenizer service..
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, EdifactTokenizer(this.document,this.elementsToDeTokenize,senderIdList,receiverIdList,Constants.TOKENIZER_METHOD_DETOKENIZE)));
    }



}