package br.com.fiap.hackathon_video.application.service;

import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticatedUserService implements GetAuthenticatedUserUseCase {

	@Override
	public String getAuthenticatedUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			log.warn("Tentativa de acesso sem autenticação");
			return null;
		}

		return authentication.getName();
	}

	@Override
	public String getAuthenticatedUserId() {
		return null;
	}
}
