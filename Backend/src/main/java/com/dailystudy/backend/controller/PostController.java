package com.dailystudy.backend.controller;

import com.dailystudy.backend.model.Post;
import com.dailystudy.backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<Post> postar(@RequestBody Post post) {
        return ResponseEntity.ok(postService.criarPost(post));
    }
}
