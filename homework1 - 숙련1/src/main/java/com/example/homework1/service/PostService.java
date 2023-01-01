package com.example.homework1.service;

import com.example.homework1.dto.CreatePostRequest;
import com.example.homework1.dto.PostResponse;
import com.example.homework1.dto.UpdatePostRequest;
import com.example.homework1.entity.Post;
import com.example.homework1.entity.User;
import com.example.homework1.jwt.JwtUtil;
import com.example.homework1.repository.PostRepository;
import com.example.homework1.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;


    // 게시글 작성(토큰 필요)
    // 하나에 하나의 기능 -> 게시물 생성만
    public void createPost(CreatePostRequest createPostRequest) {
//        Post post = new Post(createPostRequest.getTitle(), createPostRequest.getUsername(),
//                createPostRequest.getPassword(), createPostRequest.getContents());

     Post post = createPostRequest.toEntity();
      postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물 없음"));
        return new PostResponse(post);
    }


    // 전체 게시글 목록 조회(토큰 필요없음)
    public List<PostResponse> getPostList() {
        List<Post> postList = postRepository.findAllByOrderByDateCreatedDesc();
        List<PostResponse> postResponseList = new ArrayList<>();
        for (Post post : postList) {
            postResponseList.add(new PostResponse(post));
        }
        return postResponseList;
    }

    @Transactional
    public void updatePost(Long postId, UpdatePostRequest updatePostRequest, HttpServletRequest request) {
        Post postSaved = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시물 없음"));

        String token = jwtUtil.resolveToken(request);
        Claims claims;

        if (jwtUtil.validateToken(token)) {
            claims = jwtUtil.getUserInfoFromToken(token);
        } else {
            throw new NullPointerException("토큰값이 맞지 않음");
        }

        User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new NullPointerException("해당 사용자가 존재하지 않습니다.")
        );

        if(postSaved.isValidPassword(updatePostRequest.getPassword())) { // 해당 유저의 비밀 번호 확인 후
            postSaved.update(updatePostRequest.getTitle(), updatePostRequest.getUsername(), updatePostRequest.getContents());
        }else {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

    }



    public void deletePost(Long postId, Long password, HttpServletRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시물 없음"));
    }
}
