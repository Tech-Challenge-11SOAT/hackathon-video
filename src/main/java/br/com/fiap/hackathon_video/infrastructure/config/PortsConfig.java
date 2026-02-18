package br.com.fiap.hackathon_video.infrastructure.config;

import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import br.com.fiap.hackathon_video.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PortsConfig {

	@Bean
	public TokenValidationPort tokenValidationPort(JwtTokenProvider jwtTokenProvider) {
		return jwtTokenProvider;
	}
}
