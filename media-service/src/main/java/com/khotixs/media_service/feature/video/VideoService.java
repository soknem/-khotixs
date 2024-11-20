package com.khotixs.media_service.feature.video;

import com.khotixs.media_service.feature.image.dto.ImageResponse;
import com.khotixs.media_service.feature.image.dto.ImageViewResponse;
import com.khotixs.media_service.feature.video.dto.VideoResponse;
import com.khotixs.media_service.feature.video.dto.VideoViewResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import io.minio.errors.*;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.List;

public interface VideoService {

    VideoResponse uploadVideo(MultipartFile file);

    List<VideoResponse> loadAllVideos();

    VideoResponse loadVideoByName(String mediaName);

    VideoResponse deleteVideoByName(String mediaName);

    Resource downloadVideoByName(String mediaName);

    VideoViewResponse viewVideoByFileName(String fileName) throws InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, io.minio.errors.ServerException;


}
