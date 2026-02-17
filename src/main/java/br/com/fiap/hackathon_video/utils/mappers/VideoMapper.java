package br.com.fiap.hackathon_video.utils.mappers;

import org.mapstruct.Mapper;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_video.domain.video.Video;

@Mapper(componentModel = "spring")
public interface VideoMapper {

	Video jpaToDomain(JpaVideoEntity jpaVideoEntity);

	JpaVideoEntity domainToJpa(Video video);

}
