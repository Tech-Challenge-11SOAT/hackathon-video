package br.com.fiap.hackathon_video.infrastructure.security.exception;

/**
 * Exceção lançada quando o token JWT está expirado
 * HTTP Status: 401 UNAUTHORIZED
 */
public class ExpiredTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ExpiredTokenException(String message) {
		super(message);
	}

	public ExpiredTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
