package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando há uma violação de regra de negócio
 * HTTP Status: 422 UNPROCESSABLE ENTITY
 */
public class BusinessRuleException extends DomainException {

	private static final long serialVersionUID = 1L;

	public BusinessRuleException(String message) {
		super(message);
	}

	public BusinessRuleException(String message, Throwable cause) {
		super(message, cause);
	}
}
