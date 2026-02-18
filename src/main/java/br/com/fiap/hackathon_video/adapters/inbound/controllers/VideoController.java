package br.com.fiap.hackathon_video.adapters.inbound.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;

@Slf4j
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Tag(name = "Videos", description = "Endpoints para gerenciamento de vídeos")
@SecurityRequirement(name = "Bearer Authentication")
public class VideoController {

	private final VideoUseCases videoUseCases;
	private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary = "Upload de vídeo", description = "Realiza upload de um arquivo de vídeo")
	public ResponseEntity<VideoResponseDTO> upload(@Valid @ModelAttribute VideoUploadRequestDTO videoDTO) {
		String username = getAuthenticatedUserUseCase.getAuthenticatedUsername();

		log.info("Usuário {} fazendo upload de vídeo: {}", username, videoDTO.getTitle());

		VideoResponseDTO response = new VideoResponseDTO(videoUseCases.uploadVideo(videoDTO.getFile()));
		return ResponseEntity.ok(response);
	}

}
