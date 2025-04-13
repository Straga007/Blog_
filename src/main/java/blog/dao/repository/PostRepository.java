package blog.dao.repository;

import blog.model.Post;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    public void save(Post post);
    public Optional<Post> findById(Integer id);
    public List<Post> getPosts(String search, int pageSize, int pageNumber);
    public boolean hasMorePosts(String search, int pageSize, int pageNumber);
}
