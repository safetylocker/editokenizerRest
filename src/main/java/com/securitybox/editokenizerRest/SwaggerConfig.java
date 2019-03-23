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
                           "\nIn addition, the service supports a cleint to get accesss logs of other clients who has accessed the tokens",
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