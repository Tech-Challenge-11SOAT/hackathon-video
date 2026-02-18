package br.com.fiap.hackathon_video.domain.video;

import java.time.LocalDateTime;
import java.util.UUID;

public class Video {
	private UUID id;
	private UUID userId;
	private String originalFileName;
	private String s3VideoKey;
	private String s3ZipKey;
	private String status;
	private String errorMessage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Video() {
	}

	public Video(UUID id, UUID userId, String originalFileName, String s3VideoKey, String s3ZipKey, String status,
			String errorMessage, LocalDateTime createdAt, LocalDateTime updatedAt) {
		this.id = id;
		this.userId = userId;
		this.originalFileName = originalFileName;
		this.s3VideoKey = s3VideoKey;
		this.s3ZipKey = s3ZipKey;
		this.status = status;
		this.errorMessage = errorMessage;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getUserId() {
		return this.userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public String getOriginalFileName() {
		return this.originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getS3VideoKey() {
		return this.s3VideoKey;
	}

	public void setS3VideoKey(String s3VideoKey) {
		this.s3VideoKey = s3VideoKey;
	}

	public String getS3ZipKey() {
		return this.s3ZipKey;
	}

	public void setS3ZipKey(String s3ZipKey) {
		this.s3ZipKey = s3ZipKey;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
