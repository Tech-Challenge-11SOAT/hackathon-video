package br.com.fiap.hackathon_video.adapters.outbound.s3;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.com.fiap.hackathon_video.application.ports.outbound.S3StoragePort;
import br.com.fiap.hackathon_video.domain.exception.S3UploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3StorageAdapter implements S3StoragePort {

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	@Override
	public String uploadVideo(MultipartFile file, String s3Key) {
		try {
			log.info("Iniciando upload para S3: bucket={}, key={}", bucketName, s3Key);

			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(s3Key)
					.contentType(file.getContentType())
					.contentLength(file.getSize())
					.build();

			s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

			log.info("Upload concluído com sucesso: {}", s3Key);
			return s3Key;

		} catch (IOException e) {
			log.error("Erro ao ler arquivo para upload: {}", e.getMessage(), e);
			throw new S3UploadException("Erro ao ler arquivo para upload no S3", e);
		} catch (Exception e) {
			log.error("Erro ao fazer upload para S3: {}", e.getMessage(), e);
			throw new S3UploadException("Erro ao fazer upload do vídeo para S3", e);
		}
	}

	@Override
	public void deleteFile(String s3Key) {
		try {
			log.info("Deletando arquivo do S3: bucket={}, key={}", bucketName, s3Key);

			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
					.bucket(bucketName)
					.key(s3Key)
					.build();

			s3Client.deleteObject(deleteObjectRequest);

			log.info("Arquivo deletado com sucesso: {}", s3Key);

		} catch (Exception e) {
			log.error("Erro ao deletar arquivo do S3: {}", e.getMessage(), e);
			throw new S3UploadException("Erro ao deletar arquivo do S3", e);
		}
	}

	@Override
	public String generatePresignedUrl(String s3Key, int expirationMinutes) {
		try {
			log.info("Gerando URL pré-assinada: bucket={}, key={}, expiration={}min", bucketName, s3Key,
					expirationMinutes);

			GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
					.signatureDuration(Duration.ofMinutes(expirationMinutes))
					.getObjectRequest(req -> req.bucket(bucketName).key(s3Key))
					.build();

			PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
			String url = presignedRequest.url().toString();

			log.info("URL pré-assinada gerada com sucesso");
			return url;

		} catch (Exception e) {
			log.error("Erro ao gerar URL pré-assinada: {}", e.getMessage(), e);
			throw new S3UploadException("Erro ao gerar URL de download", e);
		}
	}
}

