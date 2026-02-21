package br.com.fiap.hackathon_video.application.ports.outbound.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VideoProcessingMessage {
	private UUID videoId;
	private UUID userId;
	private String s3VideoKey;
	private String originalFileName;
	private LocalDateTime createdAt;
}
