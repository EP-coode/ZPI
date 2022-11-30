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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BackendPostTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    public void testFailedCreatePost() {
        PostCreateUpdateDto post = new PostCreateUpdateDto();

        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/posts/create")
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
        post.setTitle("Tytuł");
        post.setCategoryName("Matematycy");
        post.setMarkdownContent("Krótki content");
        post.setTagNames(new HashSet<>(Arrays.asList("jedzenie", "jarmark")));
        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/posts/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(post));
            mockMvc.perform(mockRequest).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreatePostWithPhoto() {
        PostCreateUpdateDto post = new PostCreateUpdateDto();
        post.setTitle("Tytuł 2");
        post.setCategoryName("Matematycy");
        post.setMarkdownContent("Krótki content 2");
        post.setTagNames(new HashSet<>(Arrays.asList("jedzenie", "jarmark")));
        MockMultipartFile photo;
        try {
            photo = new MockMultipartFile("photo", Files.readAllBytes(Path.of("./src/main/resources/trollface.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            var mockRequest = MockMvcRequestBuilders
                    .multipart("http://localhost:8080/posts/create")
                    .file(photo)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(post));
            mockMvc.perform(mockRequest).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
