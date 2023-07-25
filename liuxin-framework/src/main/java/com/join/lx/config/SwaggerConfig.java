package com.join.lx.config;

import com.join.lx.constants.SystemConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket customDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.join.lx.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact(SystemConstants.CONTACT_NAME, SystemConstants.CONTACT_URL, SystemConstants.CONTACT_EMAIL);
        return new ApiInfoBuilder()
                .title("文档标题")
                .description("文档描述")
                .contact(contact)   // 联系
                .version("1.1.0")  // 版本
                .build();
    }
}
