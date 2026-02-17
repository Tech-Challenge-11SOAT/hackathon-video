package br.com.fiap.hackathon_video.adapters.inbound.dto.request;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoUploadRequestDTO {
	@NotNull(message = "É obrigatório enviar um arquivo de vídeo.")
	private MultipartFile file;

	private String title;
	private String description;
}
