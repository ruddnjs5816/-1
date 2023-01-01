package com.example.homework1.dto;

import com.example.homework1.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final String title;
    private final String username;
    private final String contents;
    private final LocalDateTime dateCreated;

    public PostResponse(Post post){
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.contents = post.getContents();
        this.dateCreated = post.getDateCreated();
    }

}
