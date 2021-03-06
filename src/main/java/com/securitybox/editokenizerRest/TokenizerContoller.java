package com.securitybox.editokenizerRest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.securitybox.constants.Constants;
import com.securitybox.editokenizerRest.model.AuditResponse;
import com.securitybox.editokenizerRest.model.TokenizerDocument;
import com.securitybox.models.AccessEntry;
import io.swagger.annotations.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import static com.securitybox.editokenizerRest.TokenizerApplication.*;

@RestController
@RequestMapping("/tokenizer")
@Api(value="Tokenization REST API.", description="API operations supported.")
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
    private String EdifactTokenizer(String input, JSONArray elementsToDeTokenize, String senderId, ArrayList receiverIdList,String operation){
        String response="";
        try {
            if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_TOKENIZE))
                response = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE,elementsToDeTokenize,input,senderId,receiverIdList);
            else if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_DETOKENIZE))
                response = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_DETOKENIZE,elementsToDeTokenize,input,senderId,receiverIdList);
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
    private String csvTokenizer(String input, JSONArray elementsToDeTokenize, String senderId, ArrayList receiverIdList,String csvRecordSeperator,String csvFieldSeperator,String operation){
        String response="";
        try {
            if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_TOKENIZE)) {
                response = csv.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE, elementsToDeTokenize, input, senderId, receiverIdList,csvRecordSeperator,csvFieldSeperator);
            }else if(operation.equalsIgnoreCase(Constants.TOKENIZER_METHOD_DETOKENIZE)){
                response = csv.docuemntHandler(Constants.TOKENIZER_METHOD_DETOKENIZE,elementsToDeTokenize,input,senderId,receiverIdList,csvRecordSeperator,csvFieldSeperator);
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
            notes = "1)Sample EDIFACT Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestTokenizerEDISample +
                    "\n\n2)Sample array of items to tokenize(EDIFACT)  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToTokenizeJsonEDIFACT +
                    "\n\n3)Sample CSV Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestTokenizerCSVSample +
                    "\n\n4)Sample array of items to tokenize(CSV)  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToTokenizeJsonCSV
    )
    @ApiImplicitParam(name="MessageType",example = "EDIFACT/CSV")
    public TokenizerDocument tokenize(@RequestParam(value="ElementsToTokenize",required = true) JSONArray elementsToTokenize,
                                      @RequestParam(value="SenderId",required = true) String senderId,
                                      @RequestParam(value="ReceiverIds",required = false) ArrayList<String> receiverIdList,
                                      @RequestParam(value="MessageType",required = true) String messageType,
                                      @RequestParam(value="csvRecordSeperator",required = false) String csvRecordSeperator,
                                      @RequestParam(value="csvFieldSeperator",required = false) String csvFieldSeperator,
                                      @RequestBody() String document ) {

        //call EDIFACT rokenizer service..
        if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_EDIFACT)) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, EdifactTokenizer(document, elementsToTokenize, senderId, receiverIdList, Constants.TOKENIZER_METHOD_TOKENIZE)));
        }else if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CSV)){
            if(csvFieldSeperator==null || csvRecordSeperator==null){
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template,"CSV filed/record delimeters not set correctly."));
            }else {
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template, csvTokenizer(document, elementsToTokenize, senderId, receiverIdList, csvRecordSeperator, csvFieldSeperator, Constants.TOKENIZER_METHOD_TOKENIZE)));
            }
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
            notes = "1)Sample EDIFACT Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestDeTokenizerEDISample +
                    "\n\n2)Sample array of items to de-tokenize(EDIFACT)  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToDeTokenizeJsonEDIFACT +
                    "\n\n3)Sample CSV Request : \n" +
                    "-------------------------\n" + com.securitybox.editokenizerRest.Constants.requestDeTokenizerCSVSample +
                    "\n\n4)Sample array of items to de-tokenize(CSV)  : \n" +
                    "-------------------------------------------\n" + com.securitybox.editokenizerRest.Constants.elementsToDeTokenizeJsonExampleCSV)
    @ApiImplicitParam(name="MessageType",example = "EDIFACT OR CSV")

    public TokenizerDocument detokenize(
            @RequestParam(value="ElementsToDeTokenize",required = true) JSONArray elementsToDeTokenize,
            @RequestParam(value="SenderId",required = true) String senderId,
            @RequestParam(value="MessageType",required = true) String messageType,
            @RequestParam(value="csvRecordSeperator",required = false) String csvRecordSeperator,
            @RequestParam(value="csvFieldSeperator",required = false) String csvFieldSeperator,
            @RequestBody(required = true) String document) {

        //log for debugging...
        System.out.println("Received Message : " + document);
        System.out.println("Received elements to be de-tokenized : " + elementsToDeTokenize);
        System.out.println("Received sender id : " + senderId);

        //call EDIFACT rokenizer service..
        if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_EDIFACT)) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, EdifactTokenizer(document, elementsToDeTokenize, senderId, null, Constants.TOKENIZER_METHOD_DETOKENIZE)));
        }else if(messageType.equalsIgnoreCase(Constants.DOCUMENT_TYPE_CSV)){
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template, csvTokenizer(document, elementsToDeTokenize, senderId, null,csvRecordSeperator,csvFieldSeperator, Constants.TOKENIZER_METHOD_DETOKENIZE)));

        } else {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"De-tokenization failure. Please verify the request and parameters are valid."));
        }
    }

    //******************************************************************************************
    // TOKENIZE single element
    //******************************************************************************************
    //Get a stored value of a token stored
    @ApiOperation(value = "Tokenize a given value.")
    @RequestMapping(value = "/tokenize", method = RequestMethod.GET)
    @ResponseBody
    @ApiImplicitParam(name="value",example = "Value to be tokenized.")
    public TokenizerDocument createTokenValue(
            @RequestParam(value = "value",required = true) String value,
            @RequestParam(value="SenderId",required = true) String senderId,
            @RequestParam(value="ReceiverIds",required = false) ArrayList<String> receiverIdList,
            @RequestParam(value = "maxTokenLenght",required = false,defaultValue = "0") Integer maxTokenLenght){
        try {
            //check the macimum value support by client for the token
            //Value is taken to generate token based on different algorithms in core module.
            //If the value is less than or equal to 16 , java UUID is used to create a unique token.
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,simpleTokenizer.tokenizeSingleValue(Constants.TOKENIZER_METHOD_TOKENIZE, value, senderId, receiverIdList, maxTokenLenght)));
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
    public TokenizerDocument getTokenValue(
            @RequestParam("token") String token,
            @RequestParam(value="SenderId",required = true) String senderId){
        String response = "";
        try {
            response = simpleTokenizer.deTokenizeSingleValue(Constants.TOKENIZER_METHOD_DETOKENIZE,token,senderId);
            if(response==null) {
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template, "Token Not Found"));
            }else {
                return new TokenizerDocument(counter.incrementAndGet(),
                        String.format(template, response));
            }

        } catch (JSONException e) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Token Not Found"));
        } catch (NoSuchAlgorithmException e) {
            return new TokenizerDocument(counter.incrementAndGet(),
                    String.format(template,"Token Not Found"));
        }
    }

    //******************************************************************************************
    // Access log method
    //******************************************************************************************
    //Get access logs of a given token
    @ApiOperation(value = "Request a token to be deleted.")
    @RequestMapping(value = "/token", method = RequestMethod.DELETE,produces = "application/json")
    @ResponseBody
    public TokenizerDocument deleteToken(
            @RequestParam("token") String token,
            @RequestParam(value="SenderId",required = true) String senderId){
        return  new TokenizerDocument(counter.incrementAndGet(),String.valueOf(simpleTokenizer.tokenizer.removeToken(token)));
    }

    //******************************************************************************************
    // Remove token content method
    //******************************************************************************************
    //Get access logs of a given token
    @ApiOperation(value = "Request to remove the content from token entry.")
    @RequestMapping(value = "/removeTokenEntry", method = RequestMethod.DELETE,produces = "application/json")
    @ResponseBody
    public TokenizerDocument removeTokenEntry(
            @RequestParam("token") String token,
            @RequestParam(value="SenderId",required = true) String senderId){
        return  new TokenizerDocument(counter.incrementAndGet(),String.valueOf(simpleTokenizer.tokenizer.removeTokenEntry(token,senderId)));
    }

    //******************************************************************************************
    // Access log method
    //******************************************************************************************
    //Get access logs of a given token
    @ApiOperation(value = "Request audit logs of a token.")
    @RequestMapping(value = "/audit/logs", method = RequestMethod.GET,produces = "application/json")
    @ResponseBody
    public AuditResponse getAuditLogs(
            @RequestParam("token") String token,
            @RequestParam(value="SenderId",required = true) String senderId){

        ArrayList<AccessEntry> response=new ArrayList<AccessEntry>();
        try{
            response =simpleTokenizer.tokenizer.getAccessLogs(token,senderId);
        }catch (Exception e){
            //If empty response, let the client knows that access is denied.
            //This could be either token does not exisitn or client may not have access to the token.
            response.add(new AccessEntry(new Date(),senderId,Constants.DATA_STORE_ACTION_DENIED));
        }
        return new AuditResponse(counter.incrementAndGet(),response);

    }
}