package br.com.fiap.hackathon_video.domain.exception;

import java.util.UUID;

/**
 * Exceção lançada quando um vídeo não é encontrado
 * HTTP Status: 404 NOT FOUND
 */
public class VideoNotFoundException extends ResourceNotFoundException {

	private static final long serialVersionUID = 1L;

	public VideoNotFoundException(UUID videoId) {
		super("Vídeo", videoId.toString());
	}

	public VideoNotFoundException(String message) {
		super(message);
	}
}
