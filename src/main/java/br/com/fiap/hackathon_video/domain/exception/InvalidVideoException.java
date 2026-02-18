package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando os dados do vídeo são inválidos
 * HTTP Status: 400 BAD REQUEST
 */
public class InvalidVideoException extends DomainException {

	private static final long serialVersionUID = 1L;

	public InvalidVideoException(String message) {
		super(message);
	}

	public InvalidVideoException(String message, Throwable cause) {
		super(message, cause);
	}
}
