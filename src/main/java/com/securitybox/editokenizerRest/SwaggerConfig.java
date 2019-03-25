package com.securitybox.editokenizerRest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

//API docs will be availalbe http://localhost:8080/v2/api-docs
//http://localhost:8080/swagger-ui.html
@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
    static final String requestTokenizerEDISample  ="UNA:+. ?'UNB+UNOC:3+SENDERID+RECIPIENTID+100615:0100+1006150100000++MYCOMPANY'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Sender AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@Sender.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'UNT+XX+1'UNZ+1+1006150100000'";
    static final String requestDeTokenizerEDISample="UNA:+. ?'UNB+UNOC:3+SENDERID+RECIPIENTID+100615:0100+1006150100000++MYCOMPANY'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Sender AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@Sender.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'UNT+YY+1'UNZ+1+1006150100000'";
    static final String elementsToDeTokenizeJsonExample="[{\"segmentNumber\":10, \"dataElementNumber\":5, \"dataElementPosition\":1, \"dataElementLength\":30},{\"segmentNumber\":16, \"dataElementNumber\":3, \"dataElementPosition\":2, \"dataElementLength\":20}]";
    static final String elementsToDeTokenizeJsonExampleCSV="[{\"dataElementPosition\":1, \"dataElementLength\":24},{\"dataElementPosition\":2, \"dataElementLength\":15}]";

    @Bean
    public Docket productApi() {
        //return new Docket(DocumentationType.SWAGGER_2)
                //.select()                 .apis(RequestHandlerSelectors.basePackage("com.securitybox.editokenizerRest"))
                //.paths(regex("/tokernizer.*"))
                //.build();
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.securitybox.editokenizerRest")).build()
                .apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Electronic Document Tokenization Service",
                "This is an API service to tokenize electronic form of document." +
                          "\nThe service support tokenization and de-tokenization of EDIFACT/CSV messages given the eleemnts to be tokenized." +
                           "\nIn addition, the service supports a cleint to get access logs of other clients who has accessed the tokens" +

                            "\n\nSample EDI tokenization Request" +
                            "\n--------------------------------\n"
                            + requestDeTokenizerEDISample +
                            "\n\nSample EDI tokenization Request" +
                            "\n--------------------------------\n"
                            +requestDeTokenizerEDISample +
                            "\n\nSample JSON Array for EDIFACT tokenization reqest" +
                            "\n-------------------------------------------------\n"
                            + elementsToDeTokenizeJsonExample +
                            "\n\nSample JSON Array for CSV tokenization reqest" +
                            "\n--------------------------------\n"
                            + elementsToDeTokenizeJsonExampleCSV


                ,
                "1.0.0",
                "This is non-commercial version available for public use.",
                new Contact("Security Box", "www.safetylocker.eu", "tokenizer@safetylocker.eu"),
                "General Public License", "www.safetylocker.eu/license", Collections.emptyList());
    }
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}