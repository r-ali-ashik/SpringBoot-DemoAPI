package com.aliashik.config;


import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aliashik.controller"))
                .paths(paths())
                .build()
                .apiInfo(metaData());

    }

    private Predicate<String> paths() {
        return or(regex("/v1.*"));
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Demo Rest API")
                .description("Spring Boot demo REST API")
                .version("1.0.0")
                .license("MIT License")
                .licenseUrl("aliashik.blogspot.com\"")
                .contact(new Contact("Ali Ashik", "www.facebook.com/r.ali.ashik", "ramjan.ali.ashik@gmail.com"))
                .build();
    }
}
