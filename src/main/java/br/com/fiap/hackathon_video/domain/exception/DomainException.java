package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção base para todas as exceções de domínio
 * Segue o padrão da Arquitetura Hexagonal - exceções do domínio
 */
public abstract class DomainException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected DomainException(String message) {
		super(message);
	}

	protected DomainException(String message, Throwable cause) {
		super(message, cause);
	}
}
