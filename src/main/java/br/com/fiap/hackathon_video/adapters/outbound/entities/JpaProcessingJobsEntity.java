package br.com.fiap.hackathon_video.adapters.outbound.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.hackathon_video.domain.enums.EStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "processing_jobs", indexes = {
		@Index(name = "idx_processing_jobs_video_id", columnList = "video_id"),
		@Index(name = "idx_processing_jobs_status", columnList = "status"),
		@Index(name = "idx_processing_jobs_created_at", columnList = "created_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JpaProcessingJobsEntity {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "video_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID videoId;

	@Column(name = "s3_video_key", nullable = false, length = 500)
	private String s3VideoKey;

	@Column(name = "s3_zip_key", length = 500)
	private String s3ZipKey;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EStatus status;

	@Column(name = "frame_count")
	private Integer frameCount;

	@Column(name = "error_message", length = 1000)
	private String errorMessage;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@PrePersist
	public void prePersist() {
		this.id = this.id == null ? UUID.randomUUID() : this.id;
		this.createdAt = LocalDateTime.now();
		this.status = this.status == null ? EStatus.PENDING : this.status;
	}
}
