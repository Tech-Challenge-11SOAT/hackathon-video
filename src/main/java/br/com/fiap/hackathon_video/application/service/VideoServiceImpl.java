package br.com.fiap.hackathon_video.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.ports.outbound.S3StoragePort;
import br.com.fiap.hackathon_video.application.ports.outbound.VideoProcessingPublisherPort;
import br.com.fiap.hackathon_video.application.ports.outbound.dto.VideoProcessingMessage;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoException;
import br.com.fiap.hackathon_video.domain.exception.InvalidVideoFormatException;
import br.com.fiap.hackathon_video.domain.video.EStatus;
import br.com.fiap.hackathon_video.domain.video.Video;
import br.com.fiap.hackathon_video.domain.video.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoServiceImpl implements VideoUseCases {

	private static final long MAX_FILE_SIZE = 5_000_000_000L; // 5GB
	private static final String[] ALLOWED_FORMATS = { "mp4", "avi", "mkv", "mov", "flv", "webm", "m4v" };

	private final VideoRepository videoRepository;
	private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
	private final S3StoragePort s3StoragePort;
	private final VideoProcessingPublisherPort videoProcessingPublisherPort;

	@Override
	public VideoResponseDTO uploadVideo(VideoUploadRequestDTO videoDTO) {
		try {
			this.validateVideoFile(videoDTO);

			String userId = getAuthenticatedUserUseCase.getAuthenticatedUserId();

			String s3VideoKey = "videos/" + UUID.randomUUID() + "/" + videoDTO.getFile().getOriginalFilename();

			log.info("Iniciando upload do vídeo para S3: {}", s3VideoKey);
			s3StoragePort.uploadVideo(videoDTO.getFile(), s3VideoKey);
			log.info("Upload do vídeo concluído com sucesso: {}", s3VideoKey);

			Video video = new Video(
					UUID.randomUUID(),
					UUID.fromString(userId),
					videoDTO.getFile().getOriginalFilename(),
					s3VideoKey,
					null,
					EStatus.PENDING.name(),
					null,
					LocalDateTime.now(),
					LocalDateTime.now());

			Video savedVideo = this.createVideo(video);

			VideoProcessingMessage message = VideoProcessingMessage.builder()
					.videoId(savedVideo.getId())
					.userId(savedVideo.getUserId())
					.s3VideoKey(savedVideo.getS3VideoKey())
					.originalFileName(savedVideo.getOriginalFileName())
					.createdAt(savedVideo.getCreatedAt())
					.build();

			videoProcessingPublisherPort.publishVideoProcessingRequest(message);

			log.info("Vídeo criado com sucesso: {}", savedVideo.getId());

			return new VideoResponseDTO(savedVideo.getId().toString());

		} catch (InvalidVideoException | InvalidVideoFormatException e) {
			log.warn("Validação de vídeo falhou: {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Erro ao fazer upload do vídeo", e);
			throw new InvalidVideoException("Erro ao processar upload do vídeo: " + e.getMessage(), e);
		}
	}

	@Override
	public Video createVideo(Video video) {
		if (video.getUserId() == null || video.getOriginalFileName() == null) {
			throw new InvalidVideoException("UserId e OriginalFileName são obrigatórios");
		}

		return videoRepository.save(video);
	}

	/**
	 * Valida se o arquivo de vídeo é válido
	 */
	private void validateVideoFile(VideoUploadRequestDTO videoDTO) {
		if (videoDTO.getFile() == null || videoDTO.getFile().isEmpty()) {
			throw new InvalidVideoException("Arquivo de vídeo é obrigatório");
		}

		String fileName = videoDTO.getFile().getOriginalFilename();
		if (fileName == null || fileName.isEmpty()) {
			throw new InvalidVideoException("Nome do arquivo é inválido");
		}

		// Validar formato
		String fileExtension = getFileExtension(fileName).toLowerCase();
		if (!isValidFormat(fileExtension)) {
			throw new InvalidVideoFormatException(fileName);
		}

		long fileSize = videoDTO.getFile().getSize();
		if (fileSize > MAX_FILE_SIZE) {
			throw new InvalidVideoException(
					String.format("Arquivo muito grande. Tamanho: %d bytes, Máximo: %d bytes", fileSize,
							MAX_FILE_SIZE));
		}

		if (fileSize == 0) {
			throw new InvalidVideoException("Arquivo está vazio");
		}
	}

	private String getFileExtension(String fileName) {
		int lastDot = fileName.lastIndexOf('.');
		if (lastDot == -1 || lastDot == fileName.length() - 1) {
			return "";
		}
		return fileName.substring(lastDot + 1);
	}

	private boolean isValidFormat(String extension) {
		for (String allowedFormat : ALLOWED_FORMATS) {
			if (allowedFormat.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}
}
