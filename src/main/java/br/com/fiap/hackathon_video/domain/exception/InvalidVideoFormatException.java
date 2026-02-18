package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando o formato do vídeo é inválido
 * HTTP Status: 400 BAD REQUEST
 */
public class InvalidVideoFormatException extends FileUploadException {

	private static final long serialVersionUID = 1L;

	public InvalidVideoFormatException(String fileName) {
		super(String.format("Formato de arquivo inválido: %s. Formatos aceitos: mp4, avi, mkv, mov, flv", fileName));
	}

	public InvalidVideoFormatException(String message, Throwable cause) {
		super(message, cause);
	}
}
