package br.com.fiap.hackathon_video.utils.mappers;

import org.mapstruct.Mapper;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaProcessingJobsEntity;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;

@Mapper(componentModel = "spring")
public interface ProcessingJobsMapper {

	JpaProcessingJobsEntity domainToJpa(ProcessingJobs processingJobs);

	ProcessingJobs jpaToDomain(JpaProcessingJobsEntity jpaProcessingJobsEntity);
}
