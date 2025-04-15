package controller;

import blog.controller.PostController;
import blog.model.Paging;
import blog.model.Post;
import blog.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private Model model;

    @InjectMocks
    private PostController postController;

    @Test
    void posts_shouldReturnViewNameAndAddAttributes() {
        // Arrange
        String search = "test";
        int pageSize = 10;
        int pageNumber = 1;

        List<Post> mockPosts = Arrays.asList(
                new Post(1, "Title 1", "Text 1", "/images/1.jpg", Arrays.asList("tag1", "tag2")),
                new Post(2, "Title 2", "Text 2", "/images/2.jpg", Arrays.asList("tag3"))
        );

        Paging mockPaging = new Paging();
        mockPaging.setPageNumber(pageNumber);
        mockPaging.setPageSize(pageSize);
        mockPaging.setHasNext(true);
        mockPaging.setHasPrevious(false);

        when(postService.getPosts(search, pageSize, pageNumber)).thenReturn(mockPosts);
        when(postService.hasMorePosts(search, pageSize, pageNumber)).thenReturn(true);

        // Act
        String viewName = postController.posts(search, pageSize, pageNumber, model);

        // Assert
        assertEquals("posts", viewName);

        verify(postService).getPosts(search, pageSize, pageNumber);
        verify(postService).hasMorePosts(search, pageSize, pageNumber);
    }
    @Test
    void getPost_shouldReturnPostView() {
        Post mockPost = new Post(1, "Title", "Text", "/image.jpg", List.of("tag1"));
        when(postService.getPostById(1)).thenReturn(mockPost);

        String viewName = postController.post(1, model);

        assertEquals("post", viewName);
        verify(model).addAttribute("post", mockPost);
    }
    @Test
    void addPost_shouldReturnAddPostView() {
        String viewName = postController.addPost();
        assertEquals("add-post", viewName);
    }
    @Test
    void editPost_shouldReturnAddPostViewWithPost() {
        Post mockPost = new Post(1, "Title", "Text", "/image.jpg", List.of("tag1"));
        when(postService.getPostById(1)).thenReturn(mockPost);

        String viewName = postController.editPost(1, model);

        assertEquals("add-post", viewName);
        verify(model).addAttribute("post", mockPost);
    }
    @Test
    void createNewPost_shouldCreatePostAndRedirect() throws IOException {
        MultipartFile mockFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        when(postService.createPost(anyString(), anyString(), anyString(), any(MultipartFile.class)))
                .thenReturn(1);

        String redirect = postController.createNewPost("Title", "Text", "tag1", mockFile);

        assertEquals("redirect:/posts/1", redirect);
        verify(postService).createPost("Title", "Text", "tag1", mockFile);
    }
    @Test
    void updatePost_shouldUpdatePostAndRedirect() throws IOException {
        MultipartFile mockFile = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        Post mockPost = new Post(1, "Old Title", "Old Text", "/images/old.jpg", List.of("old"));
        when(postService.getPostById(1)).thenReturn(mockPost);

        String redirect = postController.updatePost(1, "New Title", "New Text", "new", mockFile);

        assertEquals("redirect:/posts/1", redirect);
        assertEquals("New Title", mockPost.getTitle());
        assertEquals("New Text", mockPost.getText());
        assertEquals(List.of("new"), mockPost.getTags());
        verify(postService).savePost(mockPost);
    }
    @Test
    void deletePost_shouldDeletePostAndRedirect() {
        String redirect = postController.deletePost(1);
        assertEquals("redirect:/posts", redirect);
        verify(postService).deletePost(1);
    }
    @Test
    void addComment_shouldAddCommentAndRedirect() {
        String redirect = postController.addComment(1, "New comment");
        assertEquals("redirect:/posts/1", redirect);
        verify(postService).addComment(1, "New comment");
    }

    @Test
    void editComment_shouldEditCommentAndRedirect() {
        String redirect = postController.editComment(1, "Updated comment", 1);
        assertEquals("redirect:/posts/1", redirect);
        verify(postService).editComment(1, 1, "Updated comment");
    }

    @Test
    void deleteComment_shouldDeleteCommentAndRedirect() {
        String redirect = postController.deleteComment(1, 1);
        assertEquals("redirect:/posts/1", redirect);
        verify(postService).deleteComment(1, 1);
    }

    @Test
    void handleLike_shouldUpdateLikesAndRedirect() {
        String redirect = postController.handleLike(1, true);
        assertEquals("redirect:/posts/1", redirect);
        verify(postService).updateLikes(1, true);
    }

    @Test
    void getImage_shouldReturnImage() throws IOException {
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            // Arrange
            byte[] imageBytes = "test image".getBytes();
            Post mockPost = new Post(1, "Title", "Text", "/images/test.jpg", List.of("tag1"));

            when(postService.getImageByPostId(1)).thenReturn(imageBytes);
            when(postService.getPostById(1)).thenReturn(mockPost);
            filesMock.when(() -> Files.probeContentType(Paths.get("/images/test.jpg")))
                    .thenReturn("image/jpeg");

            // Act
            ResponseEntity<byte[]> response = postController.getImage(1);

            // Assert
            assertEquals(200, response.getStatusCodeValue());
            assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
            assertArrayEquals(imageBytes, response.getBody());
        }
    }
    @Test
    void getImage_shouldReturnNotFoundWhenImageMissing() throws IOException {
        when(postService.getImageByPostId(1)).thenReturn(null);

        ResponseEntity<byte[]> response = postController.getImage(1);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void posts_withEmptySearch_shouldWorkCorrectly() {
        // Arrange
        String search = "";
        int pageSize = 5;
        int pageNumber = 2;

        List<Post> mockPosts = Arrays.asList(
                new Post(3, "Title 3", "Text 3", null, Arrays.asList()));

        Paging mockPaging = new Paging();
        mockPaging.setPageNumber(pageNumber);
        mockPaging.setPageSize(pageSize);
        mockPaging.setHasNext(false);
        mockPaging.setHasPrevious(true);

        when(postService.getPosts(search, pageSize, pageNumber)).thenReturn(mockPosts);
        when(postService.hasMorePosts(search, pageSize, pageNumber)).thenReturn(false);

        // Act
        String viewName = postController.posts(search, pageSize, pageNumber, model);

        // Assert
        assertEquals("posts", viewName);
        verify(model).addAttribute("posts", mockPosts);
        verify(model).addAttribute("search", search);
        verify(model).addAttribute(eq("paging"), any(Paging.class));
    }

    @Test
    void redirectToPosts_shouldRedirectToPosts() {
        // Act
        String redirect = postController.redirectToPosts();

        // Assert
        assertEquals("redirect:/posts", redirect);
    }
}