package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando há erro no processamento do vídeo
 * HTTP Status: 500 INTERNAL SERVER ERROR
 */
public class VideoProcessingException extends DomainException {

	private static final long serialVersionUID = 1L;

	public VideoProcessingException(String message) {
		super("Erro ao processar vídeo: " + message);
	}

	public VideoProcessingException(String message, Throwable cause) {
		super("Erro ao processar vídeo: " + message, cause);
	}
}
