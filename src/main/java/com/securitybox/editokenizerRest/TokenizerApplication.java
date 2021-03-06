package com.securitybox.editokenizerRest;

import com.securitybox.constants.Constants;
import com.securitybox.ediparser.CSV;
import com.securitybox.ediparser.EDIFACT;
import com.securitybox.ediparser.SimpleTokenizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableSwagger2
@EnableWebMvc
public class TokenizerApplication {
    public static EDIFACT edifact;
    public static CSV csv;
    public static SimpleTokenizer simpleTokenizer;
    public static void main(String[] args) {
        try {
            //Initialize EDIFACT tokenizer.
            edifact = new EDIFACT();
            //Initialize CSV tokenizer.
            csv = new CSV();
            //Initialize Simple tokenizer.
            simpleTokenizer = new SimpleTokenizer();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SpringApplication.run(TokenizerApplication.class, args);
    }
}