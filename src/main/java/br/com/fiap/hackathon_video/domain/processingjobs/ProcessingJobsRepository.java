package br.com.fiap.hackathon_video.domain.processingjobs;

import java.util.List;
import java.util.UUID;

public interface ProcessingJobsRepository {

	ProcessingJobs save(ProcessingJobs processingJobs);

	ProcessingJobs findById(UUID id);

	List<ProcessingJobs> findAll();

	void deleteById(UUID id);

}
