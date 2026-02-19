package br.com.fiap.hackathon_video.application.ports.outbound;

import org.springframework.web.multipart.MultipartFile;

/**
 * Porta de saída para operações de armazenamento no S3
 */
public interface S3StoragePort {

	/**
	 * Faz upload de um vídeo para o S3
	 *
	 * @param file MultipartFile do vídeo
	 * @param s3Key Chave (caminho) do arquivo no S3
	 * @return URL ou chave do arquivo no S3
	 */
	String uploadVideo(MultipartFile file, String s3Key);

	/**
	 * Deleta um arquivo do S3
	 *
	 * @param s3Key Chave do arquivo no S3
	 */
	void deleteFile(String s3Key);

	/**
	 * Gera uma URL pré-assinada para download
	 *
	 * @param s3Key Chave do arquivo no S3
	 * @param expirationMinutes Tempo de expiração em minutos
	 * @return URL pré-assinada
	 */
	String generatePresignedUrl(String s3Key, int expirationMinutes);
}

