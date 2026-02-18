package br.com.fiap.hackathon_video.adapters.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VideoResponseDTO", description = "Resposta do upload de vídeo contendo o ID do vídeo criado")
public record VideoResponseDTO(
		@Schema(description = "Identificador único do vídeo", example = "550e8400-e29b-41d4-a716-446655440000", type = "string", format = "uuid") String videoId) {
}
