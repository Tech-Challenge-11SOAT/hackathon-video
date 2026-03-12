package br.com.fiap.hackathon_video.adapters.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VideoWithProcessingStatusResponseDTO", description = "Video do usuario com status de processamento")
public record VideoWithProcessingStatusResponseDTO(
		@Schema(description = "Identificador unico do video", example = "550e8400-e29b-41d4-a716-446655440000", type = "string", format = "uuid") String videoId,
		@Schema(description = "Nome original do arquivo", example = "video.mp4") String originalFileName,
		@Schema(description = "Status do video", example = "PENDING") String videoStatus,
		@Schema(description = "Status de processamento", example = "PROCESSING") String processingStatus) {
}
