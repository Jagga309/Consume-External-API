package com.app.ExAPi.controllers;

import com.app.ExAPi.entities.Post;
import com.app.ExAPi.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("fetch-save/{id}")
    public Post getPostsById(@PathVariable Long id){
        postService.getPosts();
        return postService.savePostById(id);
    }
}
