package br.com.fiap.hackathon_video.domain.video;

import java.util.List;
import java.util.UUID;

public interface VideoRepository {

	Video save(Video video);

	Video findById(UUID id);

	List<Video> findAll();

	void deleteById(UUID id);

}
