package br.com.fiap.hackathon_video.adapters.outbound.entities;

import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

import br.com.fiap.hackathon_video.domain.video.EStatus;

@Entity
@Table(name = "videos", indexes = {
		@Index(name = "idx_videos_user_id", columnList = "user_id"),
		@Index(name = "idx_videos_status", columnList = "status"),
		@Index(name = "idx_videos_created_at", columnList = "created_at"),
		@Index(name = "idx_videos_deleted", columnList = "deleted")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE videos SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class JpaVideoEntity {

	@Id
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
	private UUID userId;

	@Column(name = "original_filename", nullable = false, length = 255)
	private String originalFilename;

	@Column(name = "s3_video_key", nullable = false, length = 500)
	private String s3VideoKey;

	@Column(name = "s3_zip_key", length = 500)
	private String s3ZipKey;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EStatus status;

	@Column(name = "error_message", length = 1000)
	private String errorMessage;

	@Column(nullable = false)
	private boolean deleted = false;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	public void prePersist() {
		this.id = this.id == null ? UUID.randomUUID() : this.id;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
		this.status = this.status == null ? EStatus.PENDING : this.status;
	}

	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
}
