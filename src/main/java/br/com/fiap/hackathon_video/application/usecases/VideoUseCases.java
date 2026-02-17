package br.com.fiap.hackathon_video.application.usecases;

import org.springframework.web.multipart.MultipartFile;

public interface VideoUseCases {
	void processVideo(String videoPath);

	String uploadVideo(MultipartFile videoData);
}
