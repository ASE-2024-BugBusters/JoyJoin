package com.joyjoin.eventservice.service;
import com.joyjoin.eventservice.model.ImageUrl;
import com.joyjoin.eventservice.service.ImageService;
import com.joyjoin.eventservice.model.ImageRef;
import com.joyjoin.eventservice.repository.EventRepository;
import com.joyjoin.eventservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDateTime;

@Service
public class EventImageService {

    private final ImageRepository imageRepository;
    // 假设存在EventRepository来处理事件数据
    private final EventRepository eventRepository;
    private final ImageService imageService;

    @Autowired
    public EventImageService(ImageRepository imageRepository, EventRepository eventRepository, ImageService imageService) {
        this.imageRepository = imageRepository;
        this.eventRepository = eventRepository;
        this.imageService = imageService;
    }

//    public Image createImageForEvent(String eventId, String bucket, String key) {
//        String uploadUrl = imageService.getPreSignedUrlForUpload(bucket, key);
//        Image image = new Image(new ImageRef(bucket, key), List.of(new ImageUrl(uploadUrl, LocalDateTime.now().plusMinutes(10))));
//        // 这里假设 ImageRepository 已经能处理 Image 对象的保存
//        imageRepository.save(image);
//        // 假设有机制将图片ID与事件ID关联起来，可能需要修改事件实体或引入新的关联表
//        // associateImageWithEvent(eventId, image.getId());
//        return image;
//    }

    // 添加更多与事件图片管理相关的方法，例如根据事件ID查询所有图片等
}
