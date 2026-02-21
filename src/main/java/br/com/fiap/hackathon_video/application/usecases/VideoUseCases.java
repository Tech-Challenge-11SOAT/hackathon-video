package br.com.fiap.hackathon_video.application.usecases;

import java.util.List;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoResponseDTO;
import br.com.fiap.hackathon_video.adapters.inbound.dto.response.VideoWithProcessingStatusResponseDTO;
import br.com.fiap.hackathon_video.domain.video.Video;

public interface VideoUseCases {
	VideoResponseDTO uploadVideo(VideoUploadRequestDTO videoDTO);

	Video createVideo(Video video);

	List<VideoWithProcessingStatusResponseDTO> listUserVideos();
}
