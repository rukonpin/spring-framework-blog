package ru.github.rukonpin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.github.rukonpin.dto.command.CreatePostCommand;
import ru.github.rukonpin.dto.request.CreatePostApiRequest;
import ru.github.rukonpin.service.PostService;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostRestController {

    private static final Logger logger = LoggerFactory.getLogger(PostRestController.class);
    private final PostService postService;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody CreatePostApiRequest request) {
        CreatePostCommand command = new CreatePostCommand(
                request.getTitle(),
                request.getContent(),
                request.getTagIds(),
                request.getImagePath() // REST передаёт путь, не файл
        );

        long id = postService.createPost(command);
        return ResponseEntity.created(URI.create("/api/posts/" + id)).build();
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<?> incrementLikes(@PathVariable Long id) {
        logger.debug("POST /api/posts/{}/likes", id);
        postService.incrementLikes(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<?> decrementLikes(@PathVariable Long id) {
        logger.debug("DELETE /api/posts/{}/likes", id);
        postService.decrementLikes(id);
        return ResponseEntity.ok().build();
    }
}
