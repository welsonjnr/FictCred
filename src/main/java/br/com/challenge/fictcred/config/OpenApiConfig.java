package br.com.challenge.fictcred.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FictCred API")
                        .description("API para gerenciamento de clientes e propostas de crédito")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe FictCred")
                                .email("suporte@fictcred.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de Desenvolvimento"),
                        new Server().url("https://api.fictcred.com").description("Servidor de Produção")
                ));
    }
}
