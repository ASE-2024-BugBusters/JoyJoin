package com.joyjoin.postservice.repository;

import com.joyjoin.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
}
