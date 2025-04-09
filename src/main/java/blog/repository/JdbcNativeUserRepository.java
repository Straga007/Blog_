package blog.repository;

import blog.dao.repository.PostRepository;
import blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcNativeUserRepository implements PostRepository {
    @Override
    public Post save(Post post) {

        return post;
    }

    @Override
    public <S extends Post> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Post> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public Iterable<Post> findAll() {
        return null;
    }

    @Override
    public Iterable<Post> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Post entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Post> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
