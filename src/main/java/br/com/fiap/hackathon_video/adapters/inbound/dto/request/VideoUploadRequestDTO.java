package br.com.fiap.hackathon_video.adapters.inbound.dto.request;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "VideoUploadRequestDTO", description = "Requisição para upload de um arquivo de vídeo")
public class VideoUploadRequestDTO {

	@NotNull(message = "É obrigatório enviar um arquivo de vídeo.")
	@Schema(description = "Arquivo de vídeo para upload", type = "string", format = "binary", example = "video.mp4")
	private MultipartFile file;

	@Schema(description = "Título do vídeo (opcional)", example = "Meu Vídeo Incrível", type = "string")
	private String title;

	@Schema(description = "Descrição do vídeo (opcional)", example = "Uma descrição detalhada do conteúdo do vídeo", type = "string")
	private String description;
}
