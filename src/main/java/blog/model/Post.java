package blog.model;
import org.springframework.data.annotation.Id;
import javax.persistence.ElementCollection;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    @ElementCollection
    private List<String> tags;
    private List<Comment> comments;
    public Post() {}
    public Post(int id, String title, String text, String imagePath,List<String> tags){
        this.id = id;
        this.title = title;
        this.text = text;
        this.imagePath = imagePath;
        this.tags = tags;
        this.comments = new ArrayList<>();
    }

    // Добавляем методы для Thymeleaf
    public List<String> tags() {
        return this.tags;
    }

    public List<Comment> comments() {
        return this.comments;
    }

    public String textPreview() {
        return getTextPreview();
    }

    public List<String> textParts() {
        return getTextParts();
    }

    public String tagsAsText() {
        return getTagsAsText();
    }
    // Getters and Setters
    public int getId() {
        return id;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTextPreview() {
        // Return a preview of the text, e.g., first 100 characters
        return text != null && text.length() > 100 ? text.substring(0, 100) + "..." : text;
    }

    public String getTagsAsText() {
        // Return tags as a comma-separated string
        return tags != null ? String.join(", ", tags) : "";
    }
    public List<String> getTextParts() {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(text.split("\\r?\\n")); // Handles \n and \r\n
    }

}
