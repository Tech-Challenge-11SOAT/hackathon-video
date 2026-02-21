package br.com.fiap.hackathon_video.domain.processingjobs;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.hackathon_video.domain.enums.EStatus;

public class ProcessingJobs {

	private UUID id;
	private UUID videoId;
	private String s3VideoKey;
	private String s3ZipKey;
	private EStatus status;
	private Integer frameCount;
	private String errorMessage;
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private LocalDateTime createdAt;

	public ProcessingJobs() {
	}

	public ProcessingJobs(UUID id, UUID videoId, String s3VideoKey, String s3ZipKey, EStatus status, Integer frameCount,
			String errorMessage, LocalDateTime startedAt, LocalDateTime completedAt, LocalDateTime createdAt) {
		this.id = id;
		this.videoId = videoId;
		this.s3VideoKey = s3VideoKey;
		this.s3ZipKey = s3ZipKey;
		this.status = status;
		this.frameCount = frameCount;
		this.errorMessage = errorMessage;
		this.startedAt = startedAt;
		this.completedAt = completedAt;
		this.createdAt = createdAt;
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getVideoId() {
		return this.videoId;
	}

	public void setVideoId(UUID videoId) {
		this.videoId = videoId;
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

	public EStatus getStatus() {
		return this.status;
	}

	public void setStatus(EStatus status) {
		this.status = status;
	}

	public Integer getFrameCount() {
		return this.frameCount;
	}

	public void setFrameCount(Integer frameCount) {
		this.frameCount = frameCount;
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public LocalDateTime getStartedAt() {
		return this.startedAt;
	}

	public void setStartedAt(LocalDateTime startedAt) {
		this.startedAt = startedAt;
	}

	public LocalDateTime getCompletedAt() {
		return this.completedAt;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
