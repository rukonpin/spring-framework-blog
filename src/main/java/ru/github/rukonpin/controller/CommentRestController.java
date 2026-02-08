package ru.github.rukonpin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.github.rukonpin.dto.response.CommentResponse;
import ru.github.rukonpin.dto.request.CreateCommentForm;
import ru.github.rukonpin.service.CommentService;

import java.io.IOException;

@RestController
@RequestMapping("/api/comments")
public class CommentRestController {

    private static final Logger logger = LoggerFactory.getLogger(CommentRestController.class);
    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{id}/add")
    public void addComment(
            @PathVariable("id") Long id,
            @RequestBody CreateCommentForm form,
            HttpServletResponse response) throws IOException {

        logger.info("CREATE COMMENT: content={}", form.getContent());

        CommentResponse comment = commentService.addCommentAndReturn(id, form);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(comment);

        response.getWriter().write(json);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateComment(
            @PathVariable("id") Long id,
            @RequestBody CreateCommentForm form) {

        logger.info("UPDATE COMMENT: content={}", form.getContent());
        commentService.updateComment(id, form);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}
