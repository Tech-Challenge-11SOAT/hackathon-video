package br.com.fiap.hackathon_video.application.usecases;

import java.util.List;
import java.util.UUID;

import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;

public interface ProcessingJobsUseCases {
	ProcessingJobs createProcessingJob(UUID videoId, String s3VideoKey);

	List<ProcessingJobs> findByVideoIds(List<UUID> videoIds);
}
