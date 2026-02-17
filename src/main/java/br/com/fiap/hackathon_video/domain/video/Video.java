package br.com.fiap.hackathon_video.domain.video;

import java.time.LocalDateTime;
import java.util.UUID;

public class Video {
	private UUID id;

	private String filename;
	private String originalFilename;
	private Long fileSize;
	private String storagePath;

	private LocalDateTime uploadedAt;

	public Video() {
	}

	public Video(UUID id, String filename, String originalFilename, Long fileSize, String storagePath,
			LocalDateTime uploadedAt) {
		this.id = id;
		this.filename = filename;
		this.originalFilename = originalFilename;
		this.fileSize = fileSize;
		this.storagePath = storagePath;
		this.uploadedAt = uploadedAt;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	public LocalDateTime getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(LocalDateTime uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

}
