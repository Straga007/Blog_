package blog.controller;

import blog.model.Paging;
import blog.model.Post;
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
    //
    @PostMapping
    public String createPost(
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "image") MultipartFile image,
            @RequestParam(value = "tags", defaultValue = "") String tags) throws IOException {
        System.out.println("u are here 1");

        // Save the image file to the server
        String imagePath = postService.saveImage(image);
        System.out.println("u are here 2");

        Post post = new Post();
        post.setTitle(title);
        post.setText(text);
        post.setImagePath(imagePath);
        post.setTags(Arrays.asList(tags.split(",")));
        System.out.println("u are here 3");

        postService.savePost(post);
        System.out.println("u are here 4");

        // Redirect to the post's page
        return "redirect:/posts/" + post.getId();
    }
}
