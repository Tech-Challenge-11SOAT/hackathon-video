package br.com.fiap.hackathon_video.application.service;

import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserService implements GetAuthenticatedUserUseCase {

	private final TokenValidationPort tokenValidationPort;

	@Override
	public String getAuthenticatedUsername() {
		Authentication authentication = this.verifyAuthentication();

		return authentication.getName();
	}

	@Override
	public String getAuthenticatedUserId() {
		Authentication authentication = this.verifyAuthentication();

		String token = getTokenFromAuthentication(authentication);
		return tokenValidationPort.extractUserId(token);
	}

	private String getTokenFromAuthentication(Authentication authentication) {
		Object credentials = authentication.getCredentials();

		if (credentials == null) {
			log.error("Token JWT não encontrado nas credenciais da autenticação");
			throw new RuntimeException("Token JWT ausente na autenticação");
		}

		return credentials.toString();
	}

	public Authentication verifyAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			log.warn("Tentativa de acesso sem autenticação");
			throw new RuntimeException("Usuário não autenticado");
		}

		return authentication;
	}
}