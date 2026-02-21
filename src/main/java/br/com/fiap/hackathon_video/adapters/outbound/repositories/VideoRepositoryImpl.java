package br.com.fiap.hackathon_video.adapters.outbound.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import br.com.fiap.hackathon_video.adapters.outbound.entities.JpaVideoEntity;
import br.com.fiap.hackathon_video.domain.video.Video;
import br.com.fiap.hackathon_video.domain.video.VideoRepository;
import br.com.fiap.hackathon_video.utils.mappers.VideoMapper;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class VideoRepositoryImpl implements VideoRepository {

	private final JpaVideoRepository jpaVideoRepository;
	private final VideoMapper videoMapper;

	@Override
	public Video save(Video video) {
		JpaVideoEntity jpaVideoEntity = videoMapper.domainToJpa(video);

		this.jpaVideoRepository.save(jpaVideoEntity);
		return videoMapper.jpaToDomain(jpaVideoEntity);
	}

	@Override
	public Video findById(UUID id) {
		Optional<JpaVideoEntity> optionalEntity = this.jpaVideoRepository.findById(id);
		return optionalEntity.map(videoMapper::jpaToDomain).orElse(null);
	}

	@Override
	public List<Video> findByUserId(UUID userId) {
		List<JpaVideoEntity> entities = this.jpaVideoRepository.findByUserId(userId);
		return entities.stream().map(videoMapper::jpaToDomain).toList();
	}

	@Override
	public List<Video> findAll() {
		List<JpaVideoEntity> entities = this.jpaVideoRepository.findAll();
		return entities.stream().map(videoMapper::jpaToDomain).toList();
	}

	@Override
	public void deleteById(UUID id) {
		this.jpaVideoRepository.deleteById(id);
	}

}
