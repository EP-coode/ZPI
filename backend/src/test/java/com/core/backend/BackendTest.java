package com.core.backend;

import com.core.backend.dto.post.PostCreateUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BackendTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void testFailedCreatePost() {
        PostCreateUpdateDto post = new PostCreateUpdateDto();

        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(post));
            mockMvc.perform(mockRequest).andExpect(status().isForbidden());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreatePost() {
        PostCreateUpdateDto post = new PostCreateUpdateDto();
        post.setTitle("");
        post.setCategory(null);
        post.setMarkdownContent("");
        post.setImageUrl("");
        post.setTags(Collections.emptyList());

        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(post));
            mockMvc.perform(mockRequest).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
