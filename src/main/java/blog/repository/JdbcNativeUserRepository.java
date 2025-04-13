package blog.repository;

import blog.dao.repository.PostRepository;
import blog.model.Comment;
import blog.model.Post;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcNativeUserRepository implements PostRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void save(Post post) {
        if (post.getId() == 0) {
            // Вставка нового поста
            String sql = "INSERT INTO posts (title, text, image_path, likes_count) VALUES (?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getText());
                ps.setString(3, post.getImagePath());
                ps.setInt(4, post.getLikesCount());
                return ps;
            }, keyHolder);
            post.setId(keyHolder.getKey().intValue());
        } else {
            // Обновление существующего поста
            String sql = "UPDATE posts SET title = ?, text = ?, image_path = ?, likes_count = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    post.getTitle(),
                    post.getText(),
                    post.getImagePath(),
                    post.getLikesCount(),
                    post.getId());
        }

        // Обновление тегов
        updateTags(post);
        // Обновление комментариев
        updateComments(post);
    }

    private void updateTags(Post post) {
        // Удаляем старые теги для поста
        String deleteSql = "DELETE FROM tags WHERE post_id = ?";
        jdbcTemplate.update(deleteSql, post.getId());

        // Затем добавляем новые теги
        if (post.getTags() != null && !post.getTags().isEmpty()) {
            String insertSql = "INSERT INTO tags (post_id, tag) VALUES (?, ?)";
            for (String tag : post.getTags()) {
                jdbcTemplate.update(insertSql, post.getId(), tag);
            }
        }
    }

    @Override
    public Optional<Post> findById(Integer id) {
        String postSql = "SELECT * FROM posts WHERE id = ?";
        try {
            Post post = jdbcTemplate.queryForObject(postSql,
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

            // Загружаем теги для поста
            if (post != null) {
                String tagsSql = "SELECT tag FROM tags WHERE post_id = ?";
                List<String> tags = jdbcTemplate.queryForList(tagsSql, String.class, id);
                post.setTags(tags);
            }

            return Optional.ofNullable(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    private void updateComments(Post post) {
        // Delete existing comments for the post
        String deleteSql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(deleteSql, post.getId());

        // Insert new comments
        if (post.getComments() != null && !post.getComments().isEmpty()) {
            String insertSql = "INSERT INTO comments (post_id, text) VALUES (?, ?)";
            for (Comment comment : post.getComments()) {
                jdbcTemplate.update(insertSql, post.getId(), comment.getText());
                // Update comment's ID if needed (requires SELECT LAST_INSERT_ID())
                // Handle this if comments need their own IDs
            }
        }
    }
    @Override
    public List<Post> getPosts(String search, int pageSize, int pageNumber) {
        // Реализация пагинации
        int offset = (pageNumber - 1) * pageSize;
        String sql = "SELECT * FROM posts ORDER BY id DESC LIMIT ? OFFSET ?";

        List<Post> posts = jdbcTemplate.query(sql,
                new Object[]{pageSize, offset},
                (rs, rowNum) -> {
                    Post post = new Post();
                    post.setId(rs.getInt("id"));
                    post.setTitle(rs.getString("title"));
                    post.setText(rs.getString("text"));
                    post.setImagePath(rs.getString("image_path"));
                    post.setLikesCount(rs.getInt("likes_count"));
                    return post;
                });

        // Загружаем теги для каждого поста
        for (Post post : posts) {
            String tagsSql = "SELECT tag FROM tags WHERE post_id = ?";
            List<String> tags = jdbcTemplate.queryForList(tagsSql, String.class, post.getId());
            post.setTags(tags);
        }

        return posts;
    }
    @Override
    public boolean hasMorePosts(String search, int pageSize, int pageNumber) {
        int offset = pageNumber * pageSize;
        String sql = "SELECT 1 FROM posts LIMIT 1 OFFSET ?";
        try {
            jdbcTemplate.queryForObject(sql, Integer.class, offset);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

}
