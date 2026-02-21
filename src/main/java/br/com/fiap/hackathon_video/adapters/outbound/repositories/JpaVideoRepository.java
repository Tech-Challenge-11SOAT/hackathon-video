package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;

public interface JpaVideoRepository extends JpaRepository<JpaVideoEntity, UUID> {

	List<JpaVideoEntity> findByUserId(UUID userId);

}
