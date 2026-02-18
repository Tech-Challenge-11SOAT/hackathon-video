package br.com.fiap.hackathon_video.adapters.outbound.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.fiap.hackathon_video.domain.video.Video;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "videos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JpaVideoEntity {

	@Id
	@GeneratedValue
	private UUID id;

	private UUID userId;
	private String originalFileName;
	private String s3VideoKey;
	private String s3ZipKey;
	private String status;
	private String errorMessage;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public JpaVideoEntity(Video video) {
		this.id = video.getId();
		this.userId = video.getUserId();
		this.originalFileName = video.getOriginalFileName();
		this.s3VideoKey = video.getS3VideoKey();
		this.s3ZipKey = video.getS3ZipKey();
		this.status = video.getStatus();
		this.errorMessage = video.getErrorMessage();
		this.createdAt = video.getCreatedAt();
		this.updatedAt = video.getUpdatedAt();
	}
}