package com.example.demo.headerconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig { //스웨거
    @Bean
    public Docket restAPI() {
        return new Docket(DocumentationType.SWAGGER_2)

                .securityContexts(Arrays.asList((securityContext())))

                .securitySchemes(Arrays.asList(apiKey()))

                //false로 설정하면, 200, 401, 403, 404에 대한 기본 메세지 제거
                .useDefaultResponseMessages(false)

                //api 스펙이 작성되어 있는 패키지 생성
                .apiInfo(apiInfo())

                //ApiInfoBuilder 생성
                .select()

                //api: 대상 패키지 설정
                .apis(RequestHandlerSelectors.any())

                //path: 특정 path 조건에 맞는 api들을 다시 필터링하여 문서화
                .paths(PathSelectors.ant("/api/**"))

                .build();
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        consumes.add("");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return springfox
                .documentation
                .spi
                .service
                .contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    //문서를 설명하기 위한 메서드
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("그리너리 REST API")
                .description("개발 API 문서입니다. 개발, 로컬에서는 URL이 /api/api/ 이지만" +
                        " 실제 운영에서는 URL이 /api/으로 api 하나가 빠집니다." +
                        " JWT 설정에서 토큰 값을 넣을 때 앞에 \"Bearer 토큰\"으로 넣어야 작동합니다.")
                .build();
    }
}
