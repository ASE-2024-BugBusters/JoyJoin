package com.joyjoin.postservice.repository;

import com.joyjoin.postservice.model.Comment;
import com.joyjoin.postservice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
}
