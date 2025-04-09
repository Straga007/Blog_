package blog.service;

import blog.dao.repository.PostRepository;
import blog.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getPosts(String search, int pageSize, int pageNumber) {
        return List.of();
    }

    public boolean hasMorePosts(String search, int pageSize, int pageNumber) {
        return false;
    }
    public Post getPostById(int id) {
        // Implement logic to fetch a post by ID
        Post post = new Post();
        post.setId(id);
        post.setTitle("Sample Post");
        post.setText("This is a sample post text.");
        post.setImagePath("/images/sample.jpg");
        post.setLikesCount(10);
        post.setTags(List.of("tag1", "tag2"));
        post.setComments(List.of());
        return post;
    }
    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null;
        }

        // Define the directory to save the image
        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);

        // Create the directory if it doesn't exist
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save the file
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return "/images/" + fileName;
    }
    public void savePost(Post post) {
        postRepository.save(post);
    }
}
