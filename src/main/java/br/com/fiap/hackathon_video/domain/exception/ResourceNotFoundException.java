package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando um recurso não é encontrado
 * HTTP Status: 404 NOT FOUND
 */
public class ResourceNotFoundException extends DomainException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(String resourceName, String identifier) {
		super(String.format("%s não encontrado(a) com identificador: %s", resourceName, identifier));
	}
}
