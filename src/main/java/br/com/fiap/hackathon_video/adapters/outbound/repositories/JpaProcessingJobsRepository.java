package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaProcessingJobsEntity;

public interface JpaProcessingJobsRepository extends JpaRepository<JpaProcessingJobsEntity, UUID> {

	List<JpaProcessingJobsEntity> findByVideoIdIn(List<UUID> videoIds);

}
