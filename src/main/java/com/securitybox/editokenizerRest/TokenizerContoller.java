package com.securitybox.editokenizerRest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.securitybox.constants.Constants;
import io.swagger.annotations.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static com.securitybox.editokenizerRest.TokenizerApplication.*;

@RestController
@EnableWebMvc
@RequestMapping("/tokenizer")
@Api(value="Tokenization REST API.", description="API operationd supported for tokenization system.")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Request Success"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
        @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
})
public class TokenizerContoller {

    private static final String template = "%s";
    private final AtomicLong counter = new AtomicLong();

    //******************************************************************************************
    // TOKENIZE EDIFACT Documents
    //******************************************************************************************
    //Method for handling EDIFACT messages
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

    //******************************************************************************************
    // TOKENIZE CSV Documents
    //******************************************************************************************
    private String csvTokenizer(String input, JSONArray elementsToDeTokenize, ArrayList senderIdList, ArrayList receiverIdList,String operation){
        String response="";
        try {
            if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_TOKENIZE)) {
                csv.setRecordDelimeter("\n");
                csv.setFieldDelimeter(":");
                response = csv.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE, elementsToDeTokenize, input, senderIdList, receiverIdList);
            }else if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_DETOKENIZE)){
                csv.setRecordDelimeter("\n");
                csv.setFieldDelimeter(":");
                response = csv.docuemntHandler(Constants.TOKENIZER_METHOD_DETOKENIZE,elementsToDeTokenize,input,senderIdList,receiverIdList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return response;
    }

    //******************************************************************************************
    // TOKENIZE EDI Documents main method
    //******************************************************************************************
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    @PostMapping
    @RequestMapping(value = "/tokenize", method = RequestMethod.POST, produces = "application/json", consumes = "text/plain")
    @ApiOperation(value = "Tokenize an electronic message",
            notes = "Sample EDIFACT Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestTokenizerEDISample +
                    "\n\nSample array of items to tokenize  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToTokenizeJsonEDIFACT +
                    "\n\nSample CSV Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestDeTokenizerCSVSample +
                    "\n\nSample array of items to tokenize  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToDeTokenizeJsonExampleCSV
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ApiImplicitParam(name="MessageType",example = "EDIFACT OR CSV")
    public TokenizerDocument tokenize(@RequestParam(value="ElementsToTokenize",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="SenderIds",required = false) ArrayList senderIdList,
                                      @RequestParam(value="ReceiverIds",required = false) ArrayList receiverIdList,
                                      @RequestParam(value="MessageType",required = false) String messageType,
                                      @RequestBody() String document ) {


        //log for debugging...
        System.out.println("Receieved Message : " + document);
        System.out.println("Receieved elemnts to be tokenized : " + elementsToTokenize);
        System.out.println("Receieved sender id list : " + senderIdList);
        System.out.println("Receieved receiver id list : " + receiverIdList);

        //call EDIFACT rokenizer service..
        if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_EDIFACT)) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, EdifactTokenizer(document, elementsToTokenize, senderIdList, receiverIdList, Constants.TOKENIZER_METHOD_TOKENIZE)));
        }else if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CSV)){
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, csvTokenizer(document, elementsToTokenize, senderIdList, receiverIdList, Constants.TOKENIZER_METHOD_TOKENIZE)));
        }else{
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Tokenization falure. Please verify the request and parameters are valid."));
        }


    }

    //******************************************************************************************
    // DE-TOKENIZE EDI Documents main method
    //******************************************************************************************
    //request message must contain the message type, segments to be tokenized as a parameters
    //http://localhost:8080/tokenizer/edidoc?msgType=EDIFACT
    @PostMapping
    @RequestMapping(value = "/de-tokenize",method = RequestMethod.POST, produces = "application/json",consumes = "text/plain")
    @ApiOperation(value = "De-Tokenize an electronic message",
            notes = "Sample EDIFACT Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestDeTokenizerEDISample +
                    "\n\nSample array of items to de-tokenize  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToDeTokenizeJsonEDIFACT +
                    "\n\nSample CSV Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestTokenizerCSVSample +
                    "\n\nSample array of items to tokenize  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToTokenizeJsonCSV)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ApiImplicitParam(name="MessageType",example = "EDIFACT OR CSV")

    public TokenizerDocument detokenize(
            @RequestParam(value="ElementsToDeTokenize",required = true) JSONArray elementsToDeTokenize,
            @RequestParam(value="SenderIds",required = false) ArrayList senderIdList,
            @RequestParam(value="ReceiverIds",required = false) ArrayList receiverIdList,
            @RequestParam(value="MessageType",required = false) String messageType,
            @RequestBody(required = true) String document) {

        //log for debugging...
        System.out.println("Receieved Message : " + document);
        System.out.println("Receieved elemnts to be de-tokenized : " + elementsToDeTokenize);
        System.out.println("Receieved sender id list : " + senderIdList);
        System.out.println("Receieved receiver id list : " + receiverIdList);

        //call EDIFACT rokenizer service..
        if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_EDIFACT)) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, EdifactTokenizer(document, elementsToDeTokenize, senderIdList, receiverIdList, Constants.TOKENIZER_METHOD_DETOKENIZE)));
        }else if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CSV)){
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, csvTokenizer(document, elementsToDeTokenize, senderIdList, receiverIdList, Constants.TOKENIZER_METHOD_DETOKENIZE)));

        } else {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"De-tokenization falure. Please verify the request and parameters are valid."));
        }
    }

    //******************************************************************************************
    // TOKENIZE single element
    //******************************************************************************************
    //Get a stored value of a token stored
    @ApiOperation(value = "Tokenize a given value.",notes = "Minimum token lenght must be at least 32 characters for token generation.")
    @RequestMapping(value = "/tokenize", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @ApiImplicitParam(name="value",example = "Value to be tokenized.")
    public TokenizerDocument createTokenValue(
            @RequestParam(value = "value",required = true) String value,
            @RequestParam(value = "maxTokenLenght",required = false) Integer maxTokenLenght)
    {
        try {
            //check the macimum value support by client for the token
            //Value must be least 32 chars to support the MD-5 algoritm, thus request is reqjected
            if(maxTokenLenght < 32){
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template,"Token length must be at least 32 characters for token generation..."));
            } else {
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template,simpleTokenizer.tokenizeSingleValue(Constants.TOKENIZER_METHOD_TOKENIZE, value, null, null, maxTokenLenght)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Tokenization Failed..."));
        }

    }

    //******************************************************************************************
    // DE-TOKENIZE single element
    //******************************************************************************************
    //Get a stored value of a token stored
    @ApiOperation(value = "De-Tokenize a given token.")
    @RequestMapping(value = "/de-tokenize", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public TokenizerDocument getTokenValue(
            @RequestParam("token") String token) {
        String response = null;
        try {
            response = simpleTokenizer.deTokenizeSingleValue(Constants.TOKENIZER_METHOD_DETOKENIZE,token,null,null);

        } catch (JSONException e) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Token Not Found"));
        } catch (NoSuchAlgorithmException e) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Token Not Found"));
        }
        if(response.equalsIgnoreCase(token))
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Token Not Found"));
        else
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,response));
    }

    //******************************************************************************************
    // Access log method
    //******************************************************************************************
    //Get access logs of a given token
    @ApiOperation(value = "Request audit logs of a token.")
    @RequestMapping(value = "/audit/logs", method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request Success"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    public TokenizerDocument getBarBySimplePathWithRequestParam(
            @RequestParam("token") String token) {
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template,"This API method is not yet implemented !!!"));


    }




}