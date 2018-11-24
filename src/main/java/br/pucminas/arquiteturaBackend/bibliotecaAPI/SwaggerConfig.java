package br.pucminas.arquiteturaBackend.bibliotecaAPI;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build().apiInfo(apiInfo());

    }

    private ApiInfo apiInfo() {
        return new ApiInfo("Lista online de livros de seu interesse - REST API", "O seu objetivo é criar um aplicação baseada nos estilos de API, microsserviços e computação serverless que permita que desenvolvedores possam manter online uma lista de livros de seu interesse. As funções que devem ser implementadas são:\r\n"
                + "Armazenamento de um novo livro por título e ISBN na lista de favoritos, Remoção e gestão da lista de favoritos, Enriquecimento de dados através de chamadas de backends de APIs de livros, Cotação de preços em livraria virtual, Apontamento de críticas, Apontamento de reputação (1 a 5 estrelas)", "API", "Terms of service",
                new Contact("Christopher Sarmento", "www.pucminas.com.br", "chsarmento@gmail.com"), "License of API",
                "API license URL", Collections.emptyList());
    }

}
