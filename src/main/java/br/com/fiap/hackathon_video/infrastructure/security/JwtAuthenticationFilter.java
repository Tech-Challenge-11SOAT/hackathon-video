package br.com.fiap.hackathon_video.infrastructure.security;

import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Filtro de autenticação JWT para interceptar requisições HTTP
 * Componente de infraestrutura que utiliza a porta TokenValidationPort
 * Responsabilidade única: extrair e validar token JWT das requisições
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final int BEARER_PREFIX_LENGTH = 7;

	private final TokenValidationPort tokenValidationPort;

	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request,
			@NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		try {
			String token = extractTokenFromRequest(request);

			if (token != null) {
				if (tokenValidationPort.validateToken(token)) {
					this.authenticateUser(token, request);
				}
			}
		} catch (ExpiredTokenException | InvalidTokenException ex) {
			log.warn("Falha na autenticação: {}", ex.getMessage());
		} catch (Exception ex) {
			log.error("Erro inesperado ao processar autenticação JWT: {}", ex.getMessage(), ex);
		}

		filterChain.doFilter(request, response);
	}

	private String extractTokenFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
			return bearerToken.substring(BEARER_PREFIX_LENGTH);
		}

		return null;
	}

	private void authenticateUser(String token, HttpServletRequest request) {
		String username = tokenValidationPort.extractUsername(token);

		log.info("Token válido para o usuário: {}", username);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, token,
				Collections.emptyList());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
