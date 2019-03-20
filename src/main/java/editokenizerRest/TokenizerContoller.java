package editokenizerRest;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import com.securitybox.constants.Constants;
import com.securitybox.ediparser.EDIFACT;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static editokenizerRest.TokenizerApplication.edifact;

@RestController
@EnableWebMvc
public class TokenizerContoller {

    private static final String template = "EDI Tokenizer, %s!";
    private final AtomicLong counter = new AtomicLong();
    private String msgBody;



    @PostMapping
    @RequestMapping("/tokenizer/edidoc")
    public TokenizerDocument greeting(@RequestParam(value="msgType") String msgType,
                                      @RequestBody() String msgBody) {

        this.msgBody = msgBody;
        System.out.println("Recevied Message : " + this.msgBody);
        callEDIFACT(this.msgBody);
        return new TokenizerDocument(counter.incrementAndGet(),
                String.format(template, callEDIFACT(this.msgBody)));
    }

    private String callEDIFACT(String input){
        JSONArray objectToTokenized= new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.EDIFACT_SEGMENT_NUMBER,16);
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_NUMBER,5);
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_POSITION,1);
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_LENGTH,30);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        objectToTokenized.put(jsonObject);
        String tmp="";

        try {
            jsonObject.put(Constants.EDIFACT_SEGMENT_NUMBER,16);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_NUMBER,5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_POSITION,1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonObject.put(Constants.EDIFACT_DATA_ELEMENT_LENGTH,30);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> sender = new ArrayList<String>();
        sender.add("clientA");
        ArrayList<String> receiver = new ArrayList<String>();
        receiver.add("clientB");
        objectToTokenized.put(jsonObject);
        try {
            tmp = edifact.docuemntHandler(Constants.TOKENIZER_METHOD_TOKENIZE,objectToTokenized,input,sender,receiver);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return tmp;
    }


}