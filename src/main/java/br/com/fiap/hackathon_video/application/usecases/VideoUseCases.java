package br.com.fiap.hackathon_video.application.usecases;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;

public interface VideoUseCases {
	String uploadVideo(VideoUploadRequestDTO videoDTO);
}
