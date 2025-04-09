package blog.dao.repository;

import blog.model.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Integer> {
    public Post save(Post post);
}
