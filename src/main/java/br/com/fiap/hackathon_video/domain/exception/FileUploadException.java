package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando há erro no upload de arquivo
 * HTTP Status: 400 BAD REQUEST
 */
public class FileUploadException extends DomainException {

	private static final long serialVersionUID = 1L;

	public FileUploadException(String message) {
		super(message);
	}

	public FileUploadException(String message, Throwable cause) {
		super(message, cause);
	}
}
