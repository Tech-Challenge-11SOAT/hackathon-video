package br.com.fiap.hackathon_video.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Hackathon Video API")
						.version("1.0")
						.description("API para gerenciamento de vídeos com autenticação JWT"))
				.components(new Components()
						.addSecuritySchemes("Bearer Authentication",
								new SecurityScheme()
										.type(SecurityScheme.Type.HTTP)
										.scheme("bearer")
										.bearerFormat("JWT")
										.description("Insira o token JWT obtido do microsserviço hackathon-auth")));
	}
}
