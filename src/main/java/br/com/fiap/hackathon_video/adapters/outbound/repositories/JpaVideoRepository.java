package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;

public interface JpaVideoRepository extends JpaRepository<JpaVideoEntity, Object> {

}
