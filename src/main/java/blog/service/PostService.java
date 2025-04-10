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
        return postRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Post not found"));
    }
    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            return null;
        }

        String uploadDir = "src/main/resources/static/images/";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(image.getInputStream(), filePath);

        return "/images/" + fileName;
    }
    public void savePost(Post post) {
        postRepository.save(post);
    }
}
