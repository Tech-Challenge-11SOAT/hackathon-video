package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaProcessingJobsEntity;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobsRepository;
import br.com.fiap.hackathon_video.utils.mappers.ProcessingJobsMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProcessingJobsRepositoryImpl implements ProcessingJobsRepository {

	private final JpaProcessingJobsRepository jpaProcessingJobsRepository;
	private final ProcessingJobsMapper processingJobsMapper;

	@Override
	public ProcessingJobs save(ProcessingJobs processingJob) {
		JpaProcessingJobsEntity entity = processingJobsMapper.domainToJpa(processingJob);
		this.jpaProcessingJobsRepository.save(entity);
		return processingJobsMapper.jpaToDomain(entity);
	}

	@Override
	public ProcessingJobs findById(UUID id) {
		Optional<JpaProcessingJobsEntity> optionalEntity = this.jpaProcessingJobsRepository.findById(id);
		return optionalEntity.map(processingJobsMapper::jpaToDomain).orElse(null);
	}

	@Override
	public List<ProcessingJobs> findByVideoIdIn(List<UUID> videoIds) {
		List<JpaProcessingJobsEntity> entities = this.jpaProcessingJobsRepository.findByVideoIdIn(videoIds);
		return entities.stream().map(processingJobsMapper::jpaToDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		this.jpaProcessingJobsRepository.deleteById(id);
	}

	@Override
	public List<ProcessingJobs> findAll() {
		List<JpaProcessingJobsEntity> entities = this.jpaProcessingJobsRepository.findAll();
		return entities.stream().map(processingJobsMapper::jpaToDomain).toList();
	}
}
