package com.khotixs.media_service.feature.video;

import com.khotixs.media_service.domain.Image;
import com.khotixs.media_service.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long>{

    Optional<Video> findByFileName(String fileName);

    boolean existsByFileName(String fileName);
}
