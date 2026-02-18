package br.com.fiap.hackathon_video.adapters.inbound.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.ports.inbound.GetAuthenticatedUserUseCase;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController implements IVideoControllerSwagger {

	private final VideoUseCases videoUseCases;
	private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

	@Override
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<VideoResponseDTO> upload(@Valid @ModelAttribute VideoUploadRequestDTO videoDTO) {
		String username = getAuthenticatedUserUseCase.getAuthenticatedUsername();

		log.info("Usuário {} fazendo upload de vídeo: {}", username, videoDTO.getTitle());

		VideoResponseDTO response = videoUseCases.uploadVideo(videoDTO);
		return ResponseEntity.accepted().body(response);
	}

}
