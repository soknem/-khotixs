package com.khotixs.media_service.feature.image;

import com.khotixs.media_service.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long>{

    Optional<Image> findByFileName(String fileName);

    boolean existsByFileName(String fileName);
}
