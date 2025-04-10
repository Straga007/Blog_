package blog.repository;

import blog.dao.repository.PostRepository;
import blog.model.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;

@Repository
public class JdbcNativeUserRepository implements PostRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Post save(Post post) {
        String sql = "INSERT INTO posts (title, text, image_path, likes_count) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                post.getTitle(),
                post.getText(),
                post.getImagePath(),
                post.getLikesCount()
        );
        post.setId(jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return post;
    }


    @Override
    public <S extends Post> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Post> findById(Integer id) {
        String sql = "SELECT * FROM posts WHERE id = ?";
        Post post = jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                (rs, rowNum) -> {
                    Post p = new Post();
                    p.setId(rs.getInt("id"));
                    p.setTitle(rs.getString("title"));
                    p.setText(rs.getString("text"));
                    p.setImagePath(rs.getString("image_path"));
                    p.setLikesCount(rs.getInt("likes_count"));
                    return p;
                }
        );
        return Optional.ofNullable(post);
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
