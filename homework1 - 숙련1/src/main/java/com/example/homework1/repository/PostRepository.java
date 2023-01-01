package com.example.homework1.repository;

import com.example.homework1.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByDateCreatedDesc(); // 전체 게시글 내림차순
    Optional<Post> findById(Long postId); // 특정 게시글 찾기
}
