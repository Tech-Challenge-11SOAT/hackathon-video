package br.com.fiap.hackathon_video.infrastructure.security;

import br.com.fiap.hackathon_video.application.ports.outbound.TokenValidationPort;
import br.com.fiap.hackathon_video.infrastructure.security.exception.ExpiredTokenException;
import br.com.fiap.hackathon_video.infrastructure.security.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class JwtTokenProvider implements TokenValidationPort {

	@Value("${jwt.secret}")
	private String secret;

	@Override
	public boolean validateToken(String token) {
		try {
			this.parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			log.error("Token expirado: {}", e.getMessage());
			throw new ExpiredTokenException("Token JWT expirado", e);
		} catch (JwtException e) {
			log.error("Token inválido: {}", e.getMessage());
			throw new InvalidTokenException("Token JWT inválido ou mal formatado", e);
		} catch (Exception e) {
			log.error("Erro ao validar token: {}", e.getMessage());
			throw new InvalidTokenException("Erro ao processar token JWT", e);
		}
	}

	@Override
	public String extractUsername(String token) {
		try {
			Claims claims = this.parseClaims(token);
			return claims.getSubject();
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("Token JWT expirado", e);
		} catch (JwtException e) {
			throw new InvalidTokenException("Não foi possível extrair username do token", e);
		}
	}

	@Override
	public String extractUserId(String token) {
		try {
			Claims claims = parseClaims(token);
			return claims.get("userId", String.class);
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException("Token JWT expirado", e);
		} catch (JwtException e) {
			throw new InvalidTokenException("Não foi possível extrair userId do token", e);
		}
	}

	private Claims parseClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}
}
