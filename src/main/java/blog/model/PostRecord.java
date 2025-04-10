package blog.model;
import org.springframework.web.multipart.MultipartFile;

public class PostRecord {
    private String title;
    private String text;
    private String tags;
    private MultipartFile image;

    public PostRecord() {}

    public PostRecord(String title, String text, String tags, MultipartFile image) {
        this.title = title;
        this.text = text;
        this.tags = tags;
        this.image = image;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
    public MultipartFile getImage() { return image; }
    public void setImage(MultipartFile image) { this.image = image; }
}
