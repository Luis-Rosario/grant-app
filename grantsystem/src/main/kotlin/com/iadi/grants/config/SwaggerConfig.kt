package com.iadi.grants.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket


@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    fun apiInfo(): ApiInfo =
            ApiInfoBuilder()
                    .title("GRANT APPLICATION API")
                    .description("Grant application management system.").contact((Contact("João Monteiro 50576, João Fernandes 49834, Luís Rosário 50547","","")))
                    .version("0.9").build()

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .paths(PathSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("com.iadi.grants"))
            .build()


}