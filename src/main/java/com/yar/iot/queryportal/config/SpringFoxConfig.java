package com.yar.iot.queryportal.config;

import com.yar.iot.queryportal.config.security.RegistrationAndLoginFormOperations;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
import springfox.documentation.spring.web.scanners.ApiDescriptionReader;
import springfox.documentation.spring.web.scanners.ApiListingScanner;
import springfox.documentation.spring.web.scanners.ApiModelReader;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 */
@Configuration
@RequiredArgsConstructor
@EnableSwagger2
public class SpringFoxConfig {

    private static final String AUTHORIZATION_SCOPE = "global";
    private static final String PASS_AS_TYPE = "header";
    private static final String AUTHORIZATION_VALUE = "Authorization";
    private static final String AUTHORIZATION_SCOPE_DESCRIPTION = "accessEverything";
    private static final String APP_NAME_KEY = "spring.application.name";
    private static final String DESCRIPTION_KEY = "spring.application.description";
    private static final String VERSION_KEY = "spring.application.version";
    private static final String TERMS = "spring.application.version";
    private static final String LICENSE = "application.license";
    private static final String LICENSE_URL = "application.license-url";
    private static final String CONTACT_NAME = "application.contact-name";
    private static final String CONTACT_EMAIL = "application.contact-email";
    private static final String CONTACT_URL = "application.contact-url";
    private static final String SWAGGER_BASE_URL_CONTEXT = "/docs";
    private static final String SWAGGER_URL = "/swagger";
    private static final String SWAGGER_REDIRECTION = "redirect:swagger-ui.html";

    private final Environment environment;

    /**
     * The documentation builder bean
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiKey apiKey() {
        return new ApiKey(AUTHORIZATION_VALUE, AUTHORIZATION_VALUE, PASS_AS_TYPE);
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(
                new SecurityReference(
                        AUTHORIZATION_VALUE,
                        new AuthorizationScope[]{new AuthorizationScope(AUTHORIZATION_SCOPE, AUTHORIZATION_SCOPE_DESCRIPTION)}
                )
        );
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                environment.getRequiredProperty(APP_NAME_KEY),
                environment.getRequiredProperty(DESCRIPTION_KEY),
                environment.getRequiredProperty(VERSION_KEY),
                environment.getRequiredProperty(TERMS),
                new Contact(environment.getRequiredProperty(CONTACT_NAME),
                        environment.getRequiredProperty(CONTACT_URL),
                        environment.getRequiredProperty(CONTACT_EMAIL)),
                environment.getRequiredProperty(LICENSE),
                environment.getRequiredProperty(LICENSE_URL),
                Collections.emptyList());
    }


    /**
     * Create API Listening Scanenr Bean
     *
     * @return {@link ApiListingScanner}
     */
    @Primary
    @Bean
    public ApiListingScanner addExtraOperations(
            final ApiDescriptionReader apiDescriptionReader,
            final ApiModelReader apiModelReader,
            final DocumentationPluginsManager pluginsManager) {
        return new RegistrationAndLoginFormOperations(apiDescriptionReader, apiModelReader, pluginsManager);
    }

    /**
     * A custom controller for swagger
     *
     * @return {@link ApiListingScanner}
     */
    @Controller
    @RequestMapping(SWAGGER_BASE_URL_CONTEXT)
    public static class SwaggerController {

        /**
         * Redirection to swagger page
         */
        @RequestMapping(SWAGGER_URL)
        public String swagger() {
            return SWAGGER_REDIRECTION;
        }
    }
}
