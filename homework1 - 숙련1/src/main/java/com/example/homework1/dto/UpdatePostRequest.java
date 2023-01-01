package com.example.homework1.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatePostRequest {

    private String title;
    private String username;
    private String contents;
    private Long password;

}
