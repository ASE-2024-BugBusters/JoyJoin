package com.joyjoin.eventservice.repository;
import java.util.List;
import java.util.UUID;
import com.joyjoin.eventservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {
    // 可以添加根据eventId查找图片的方法
    List<Image> findByEventId(UUID eventId);
}
