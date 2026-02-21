package br.com.fiap.hackathon_video.application.usecases;

import java.util.UUID;

import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;

public interface ProcessingJobsUseCases {
	ProcessingJobs createProcessingJob(UUID videoId, String s3VideoKey);
}
