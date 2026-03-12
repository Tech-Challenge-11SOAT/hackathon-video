package br.com.fiap.hackathon_video.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.fiap.hackathon_video.application.usecases.ProcessingJobsUseCases;
import br.com.fiap.hackathon_video.domain.enums.EStatus;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobs;
import br.com.fiap.hackathon_video.domain.processingjobs.ProcessingJobsRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProcessingJobsServiceImpl implements ProcessingJobsUseCases {

	private final ProcessingJobsRepository processingJobsRepository;

	@Override
	public ProcessingJobs createProcessingJob(UUID videoId, String s3VideoKey) {
		ProcessingJobs processingJobs = new ProcessingJobs(
				UUID.randomUUID(),
				videoId,
				s3VideoKey,
				null,
				EStatus.PENDING,
				null,
				null,
				null,
				null,
				LocalDateTime.now());

		return processingJobsRepository.save(processingJobs);
	}

	@Override
	public List<ProcessingJobs> findByVideoIds(List<UUID> videoIds) {
		if (videoIds == null || videoIds.isEmpty()) {
			return List.of();
		}

		return processingJobsRepository.findByVideoIdIn(videoIds);
	}
}
