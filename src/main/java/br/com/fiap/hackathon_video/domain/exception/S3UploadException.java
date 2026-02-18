package br.com.fiap.hackathon_video.domain.exception;

/**
 * Exceção lançada quando há erro ao fazer upload para S3
 * HTTP Status: 500 INTERNAL SERVER ERROR
 */
public class S3UploadException extends FileUploadException {

	private static final long serialVersionUID = 1L;

	public S3UploadException(String message) {
		super("Erro ao fazer upload do arquivo para o servidor: " + message);
	}

	public S3UploadException(String message, Throwable cause) {
		super("Erro ao fazer upload do arquivo para o servidor: " + message, cause);
	}
}
