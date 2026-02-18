package br.com.fiap.hackathon_video.utils.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_video.domain.video.Video;

@Mapper(componentModel = "spring")
public interface VideoMapper {

	@Mapping(source = "originalFileName", target = "originalFilename")
	JpaVideoEntity domainToJpa(Video video);

	@Mapping(source = "originalFilename", target = "originalFileName")
	Video jpaToDomain(JpaVideoEntity jpaVideoEntity);

}
