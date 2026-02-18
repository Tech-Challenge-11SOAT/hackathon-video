package br.com.fiap.hackathon_video.application.service;

import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_video.adapters.inbound.dto.request.VideoUploadRequestDTO;
import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;
import br.com.fiap.hackathon_video.domain.video.VideoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoUseCases {

	private final VideoRepository videoRepository;

	@Override
	public String uploadVideo(VideoUploadRequestDTO videoDTO) {

		return null;
	}
}
