package br.com.fiap.hackathon_video.domain.exception;

import java.util.UUID;

/**
 * Exceção lançada quando um usuário não é encontrado
 * HTTP Status: 404 NOT FOUND
 */
public class UserNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(UUID userId) {
		super("Usuário", userId.toString());
	}

	public UserNotFoundException(String username) {
		super("Usuário não encontrado: " + username);
	}
}
