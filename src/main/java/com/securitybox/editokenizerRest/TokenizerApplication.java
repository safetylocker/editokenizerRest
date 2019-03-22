package com.securitybox.editokenizerRest;

import com.securitybox.ediparser.EDIFACT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableSwagger2
public class TokenizerApplication {
    public static EDIFACT edifact;
    public static void main(String[] args) {
        try {
            edifact = new EDIFACT();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SpringApplication.run(TokenizerApplication.class, args);
    }
}