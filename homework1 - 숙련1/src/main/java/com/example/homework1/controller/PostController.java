package com.example.homework1.controller;

import com.example.homework1.dto.CreatePostRequest;
import com.example.homework1.dto.PostResponse;
import com.example.homework1.dto.UpdatePostRequest;
import com.example.homework1.entity.UserRoleEnum;
import com.example.homework1.jwt.JwtUtil;
import com.example.homework1.service.PostService;
import com.example.homework1.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtUtil jwtUtil;

    // 게시글 생성(토큰 필요)
    @Transactional
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void createPost(@RequestBody CreatePostRequest createPostRequest, HttpServletRequest request) {
        //1. Request에서 토큰 가져오기
        String token = jwtUtil.resolveToken(request);

        //2. 토큰 유효성 검증 and 토큰에서 주인을 꺼냄. 주인을 신뢰할 수 있다.
        // jwt는 DB에 접근하지 않는 것이 좋다. DB에 자주 접근하는 것은 리소스 낭비가 심하다.
        Claims claims;
        if(token!= null){
            // 토큰이 validate 타당 하다면 위조가 될 수 없는 정상적인 토큰이 맞다
            if(jwtUtil.validateToken(token)){
                claims = jwtUtil.getUserInfoFromToken(token);
            }else {
                // 403
                throw new NullPointerException("토큰값 옳지 않음");
            }
            // 토큰에 의해서 요청한 사람
//            String username = claims.getSubject();
            postService.createPost(createPostRequest);
        }else {
            // 401
            throw new NullPointerException("토큰이 존재 하지 않습니다.");
        }
    }

    // 게시글 선택 조회
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable Long postId){
        return postService.getPost(postId);
    }

    // 전체 게시글 조회
    @GetMapping("/posts")
    public List<PostResponse> getPostList(){
        return postService.getPostList();
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}")
    public void updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest updatePostRequest, HttpServletRequest request){
        postService.updatePost(postId, updatePostRequest, request);
    }

    // 게시글 선택 삭제
    @DeleteMapping("/posts/{postId}")
    public void deletePost(@PathVariable Long postId, @RequestParam Long password, HttpServletRequest request){
        postService.deletePost(postId, password, request);
    }

}
