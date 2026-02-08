package ru.github.rukonpin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.github.rukonpin.dto.command.CreatePostCommand;
import ru.github.rukonpin.dto.command.EditPostCommand;
import ru.github.rukonpin.dto.request.CreateCommentForm;
import ru.github.rukonpin.dto.request.CreatePostForm;
import ru.github.rukonpin.dto.request.EditPostForm;
import ru.github.rukonpin.dto.response.PostCardResponse;
import ru.github.rukonpin.service.CommentService;
import ru.github.rukonpin.service.FileStorageService;
import ru.github.rukonpin.service.PostService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostViewController {

    private static final Logger logger = LoggerFactory.getLogger(PostViewController.class);
    private final PostService postService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;

    @Autowired
    public PostViewController(PostService postService, CommentService commentService, FileStorageService fileStorageService) {
        this.postService = postService;
        this.commentService = commentService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String posts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "categories", required = false) String searchTag,
            Model model) {

        List<PostCardResponse> posts = postService.getPosts(page, size, searchTag);

        model.addAttribute("posts", posts);
        model.addAttribute("allTags", postService.getAllTags());
        model.addAttribute("filterTags", postService.getTagsForFilter());
        model.addAttribute("totalPages", postService.getTotalPages(size, searchTag));
        model.addAttribute("currentPage", page);
        model.addAttribute("size", size);
        model.addAttribute("createPostForm", new CreatePostForm());

        posts.forEach(p -> logger.debug("Post: id={}, title={}, tags={}, likes={}, comments={}",
                p.getId(),
                p.getTitle(),
                p.getTags(),
                p.getLikesCount(),
                p.getCommentsCount()));

        return "posts";
    }

    @GetMapping("/{id}")
    public String readPost(@PathVariable Long id, Model model) {

        model.addAttribute("post", postService.getPost(id));
        model.addAttribute("allTags", postService.getAllTags());
        model.addAttribute("editPostForm", postService.getEditPost(id));
        model.addAttribute("comments", commentService.getComments(id));
        model.addAttribute("countComments", commentService.countComments(id));
        model.addAttribute("createPostForm", new CreatePostForm());
        model.addAttribute("createCommentForm", new CreateCommentForm());
        return "post";
    }

    @PostMapping("/add")
    public String addPost(@ModelAttribute CreatePostForm form) {

        logger.info("CREATE POST: title={}, tags={}, content={}, image={}, imagePath={}",
                form.getTitle(),
                form.getTagIds(),
                form.getContent(),
                form.getImage());

        try {
            String imagePath = fileStorageService.saveImage(form.getImage());

            CreatePostCommand command = new CreatePostCommand(
                    form.getTitle(),
                    form.getContent(),
                    form.getTagIds(),
                    imagePath
            );

            postService.createPost(command);
            return "redirect:/posts";

        } catch (IOException e) {
            return "redirect:/posts?error=upload";
        }
    }

    @PostMapping("/{id}/edit")
    public String updatePost(@PathVariable("id") Long id, @ModelAttribute EditPostForm form) throws IOException {
        logger.info("Полученные теги (до обработки): {}", form.getTagIds());
        logger.info("IMAGE EMPTY = {}", form.getImage() == null || form.getImage().isEmpty());

        String imagePath = null;

        if (form.getImage() != null && !form.getImage().isEmpty()) {
            imagePath = fileStorageService.saveImage(form.getImage());
        }
        logger.info("title={}, tags={}, content={}", form.getTitle(), form.getTagIds(), form.getContent());

        EditPostCommand command = new EditPostCommand(
                form.getTitle(),
                form.getContent(),
                form.getTagIds(),
                imagePath);

        postService.updatePost(id, command);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}")
    public String deletePost(@PathVariable("id") Long id,
                             @RequestParam(value = "_method", required = false) String method) {

        if ("delete".equalsIgnoreCase(method)) {
            postService.deletePost(id);
        }

        return "redirect:/posts";
    }

}