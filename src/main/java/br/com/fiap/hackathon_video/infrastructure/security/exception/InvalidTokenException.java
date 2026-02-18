package br.com.fiap.hackathon_video.infrastructure.security.exception;

/**
 * Exceção lançada quando há problemas com autenticação JWT
 * HTTP Status: 401 UNAUTHORIZED
 */
public class InvalidTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message) {
		super(message);
	}

	public InvalidTokenException(String message, Throwable cause) {
		super(message, cause);
	}
}
