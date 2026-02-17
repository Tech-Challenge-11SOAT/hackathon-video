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

	private String filename;
	private String originalFilename;
	private Long fileSize;
	private String storagePath;

	@Column(name = "uploaded_at")
	private LocalDateTime uploadedAt;

	@PrePersist
	public void prePersist() {
		uploadedAt = LocalDateTime.now();
	}

	public JpaVideoEntity(Video video) {
		this.id = video.getId();
		this.filename = video.getFilename();
		this.originalFilename = video.getOriginalFilename();
		this.fileSize = video.getFileSize();
		this.storagePath = video.getStoragePath();
		this.uploadedAt = video.getUploadedAt();
	}
}