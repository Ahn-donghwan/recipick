package com.ahndonghwan.backend.post.repository;

import com.ahndonghwan.backend.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
