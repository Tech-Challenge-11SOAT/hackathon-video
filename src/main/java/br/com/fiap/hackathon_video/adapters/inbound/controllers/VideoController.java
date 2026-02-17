package br.com.fiap.hackathon_video.adapters.inbound.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;

@Slf4j
@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
@Tag(name = "Videos", description = "Video management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class VideoController {

	private final VideoUseCases videoUseCases;

	@PostMapping("/upload")
	public VideoResponseDTO upload(@RequestBody VideoUploadRequestDTO videoDTO) {

		return new VideoResponseDTO(this.videoUseCases.uploadVideo(videoDTO.getFile()));
	}

}
