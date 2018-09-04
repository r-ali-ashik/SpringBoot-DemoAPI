package com.aliashik.config;


import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
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
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
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
   /*             .version("1.0.0")
                .license("MIT License")
                .licenseUrl("https://opensource.org/licenses/MIT")
                .contact(new Contact("Ali Ashik", "www.facebook.com/r.ali.ashik", "ramjan.ali.ashik@gmail.com"))*/
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/static/swagger/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/static/swagger/webjars/");
    }
}
