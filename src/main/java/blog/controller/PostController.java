package blog.controller;

import blog.model.Paging;
import blog.model.Post;
import blog.model.PostRecord;
import blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String redirectToPosts() {
        return "redirect:/posts";
    }

    @GetMapping
    public String posts(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNumber,
            Model model) {

        List<Post> posts = postService.getPosts(search, pageSize, pageNumber);

        Paging paging = new Paging();
        paging.setPageNumber(pageNumber);
        paging.setPageSize(pageSize);
        paging.setHasNext(postService.hasMorePosts(search, pageSize, pageNumber));
        paging.setHasPrevious(pageNumber > 1);

        // Add attributes to model
        model.addAttribute("posts", posts);
        model.addAttribute("search", search);
        model.addAttribute("paging", paging);

        return "posts";
    }
    @GetMapping("/{id}")
    public String post(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);

        // Add post to model
        model.addAttribute("post", post);

        return "post";
    }
    @GetMapping("/add")
    public String addPost() {
        return "add-post";
    }
    // добавить сохр в базу данных
    @PostMapping
    public String createNewPost(@ModelAttribute PostRecord postRecord) throws IOException {
        String imagePath = postService.saveImage(postRecord.getImage());

        Post post = new Post();
        post.setTitle(postRecord.getTitle());
        post.setText(postRecord.getText());
        post.setImagePath(imagePath);
        post.setTags(Arrays.asList(postRecord.getTags().split(",")));

        postService.savePost(post);
        return "redirect:/posts/" + post.getId();
    }
}
