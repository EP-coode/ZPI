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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", "tytuł");
        paramsMap.add("categoryName", "Fizycy");
        paramsMap.add("markdownContent", "Krótki content");
        paramsMap.add("tagNames", "jedzenie");
        paramsMap.add("tagNames", "jarmark");
        try {
            var mockRequest = MockMvcRequestBuilders
                    .multipart("http://localhost:8080/posts/create")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .params(paramsMap);
            mockMvc.perform(mockRequest).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreatePostWithPhoto() {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("title", "tytuł 2");
        paramsMap.add("categoryName", "Matematycy");
        paramsMap.add("markdownContent", "Krótki content 2");
        paramsMap.add("tagNames", "jedzenie");
        paramsMap.add("tagNames", "jarmark");

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
                    .params(paramsMap)
                    .contentType(MediaType.MULTIPART_FORM_DATA);
            mockMvc.perform(mockRequest).andExpect(status().isCreated());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
