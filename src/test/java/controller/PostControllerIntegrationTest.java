package controller;

import blog.configuration.WebConfiguration;
import blog.service.PostService;
import controller.spring.BlogApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BlogApplication.class)
@AutoConfigureMockMvc
@Import({WebConfiguration.class, TestWebConfig.class})
class PostControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void posts_shouldReturnPostsView() throws Exception {
        given(postService.getPosts(anyString(), anyInt(), anyInt())).willReturn(List.of());
        given(postService.hasMorePosts(anyString(), anyInt(), anyInt())).willReturn(false);

        mockMvc.perform(get("/posts")
                        .param("search", "test")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts", "search", "paging"));
    }
}