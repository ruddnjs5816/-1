package com.example.homework1.dto;

import com.example.homework1.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostRequest {
    private String title;
    private String username;
    private Long password;
    private String contents;

    public CreatePostRequest(String title, String username, Long password, String contents) {
        this.title = title;
        this.username = username;
        this.password = password;
        this.contents = contents;
    }

    public Post toEntity(){
        return new Post(this.title, this.username, this.password, this.contents);
    }
}
