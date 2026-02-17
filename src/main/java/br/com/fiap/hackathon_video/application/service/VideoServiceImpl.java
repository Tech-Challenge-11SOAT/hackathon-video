package br.com.fiap.hackathon_video.application.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.fiap.hackathon_video.application.usecases.VideoUseCases;
import br.com.fiap.hackathon_video.domain.video.VideoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoUseCases {

	private final VideoRepository videoRepository;

	@Override
	public void processVideo(String videoPath) {
	}

	@Override
	public String uploadVideo(MultipartFile videoData) {
		return null;
	}
}
