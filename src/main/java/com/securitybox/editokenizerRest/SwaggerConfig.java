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
    static final String requestTokenizerEDISample="UNA:+.? 'UNB+UNOC:3+UNIFAUN+RECIPIENTID+100615:0100+1006150100000++UNIFAUN'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'FTX+DIN+++Delivery information'CNT+7:7.000:KGM'CNT+26:0.500:MTQ'CNT+ZLM:0.40:MTR'CNT+Z12:3:PCE'CNT+11:3:PCE'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Unifaun AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@unifaun.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'COM+012-3456789:TE'COM+012-9876543:FX'COM+test@test.com:EM'COM+07353289222:ZMS'GID+1+1:CT'FTX+AAA+++Electronics'FTX+PAC+++9'MEA+WT++KGM:5.000'MEA+VOL++MTQ:0.300'PCI+18+373925550000420599'GID+2+2:CT'FTX+AAA+++Electronics'FTX+PAC+++9'MEA+WT++KGM:2.000'MEA+VOL++MTQ:0.200'PCI+18+373925550000420605:373925550000420612'EQD+EFP'EQN+1'UNT+39+1'UNZ+1+1006150100000'";
    static final String requestDeTokenizerEDISample="UNA:+.? 'UNB+UNOC:3+UNIFAUN+RECIPIENTID+100615:0100+1006150100000++UNIFAUN'UNH+1+IFTMIN:S:93A:UN'BGM+340+0000001339+9'DTM+137:20100615:102'TSR+PCO'FTX+DIN+++Delivery information'CNT+7:7.000:KGM'CNT+26:0.500:MTQ'CNT+ZLM:0.40:MTR'CNT+Z12:3:PCE'CNT+11:3:PCE'RFF+AAO:Receivers reference'RFF+CU:Shipment reference'TDT+20'NAD+CZ+123456++Unifaun AB+Box 2326 +GEBORG++40315+SE'CTA+IC+:John Doe'COM+031-581600:TE'COM+031-7581605:FX'COM+info@unifaun.com:EM'NAD+CN+++Testmottagaren AB+Sdervsvn 12 +VTRA FRUNDA++42651+SE'CTA+IC+:Bjrn Svensson'COM+012-3456789:TE'COM+012-9876543:FX'COM+test@test.com:EM'COM+07353289222:ZMS'GID+1+1:CT'FTX+AAA+++Electronics'FTX+PAC+++9'MEA+WT++KGM:5.000'MEA+VOL++MTQ:0.300'PCI+18+373925550000420599'GID+2+2:CT'FTX+AAA+++Electronics'FTX+PAC+++9'MEA+WT++KGM:2.000'MEA+VOL++MTQ:0.200'PCI+18+373925550000420605:373925550000420612'EQD+EFP'EQN+1'UNT+39+1'UNZ+1+1006150100000'";
    static final String elementsToDeTokenizeJsonExample="[{\"segmentNumber\":16, \"dataElementNumber\":5, \"dataElementPosition\":1, \"dataElementLength\":20},{\"segmentNumber\":28, \"dataElementNumber\":5, \"dataElementPosition\":1, \"dataElementLength\":20}]";

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
                          "\nThe service support tokenization and de-tokenization of EDIFACT messages given the elemnts to be tokenized." +
                           "\nIn addition, the service supports a cleint to get accesss logs of other clients who has accessed the tokens" +
                           "\nSample EDI tokenization Request " + requestDeTokenizerEDISample +
                           "\nSample EDI tokenization Request " + requestDeTokenizerEDISample +
                           "\nSample EDI Tokenizer JSON Array " + elementsToDeTokenizeJsonExample
                ,
                "1.0.0",
                "This is non-commercial version available for public use.",
                new Contact("Security Box", "www.securitybox.se", "tokenizer@safetylocker.eu"),
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