package com.core.backend;

import com.core.backend.dto.filter.PostFilters;
import com.core.backend.dto.filter.PostOrdering;
import com.core.backend.dto.post.PostDto;
import com.core.backend.dto.post.PostWithPaginationDto;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BackendPostTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PostService postService;
    @Autowired
    ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "USER")
    @Transactional
    public void testGetAll() {
        PostFilters postFilters = new PostFilters();
        postFilters.setOrderBy(PostOrdering.LIKES_ASC);
        postFilters.setTagNames(new String[]{"jarmark"});
        postFilters.setMaxPostDaysAge(5);
        postFilters.setCategoryId("Fizycy");
        postFilters.setCategoryGroupId("Prowadzący");
        postFilters.setCreatorId(1L);

        PostDto post1, post2;
        try {
            post1 = postService.getPostByPostId("-2");
            post2 = postService.getPostByPostId("-1");
        } catch (WrongIdException | NoIdException | NoPostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        PostWithPaginationDto expectedResult = new PostWithPaginationDto(List.of(post1, post2),2);

        try {
            var mockRequest = MockMvcRequestBuilders
                    .post("http://localhost:8080/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(postFilters));
            MockHttpServletResponse result = mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn().getResponse();
            result.setCharacterEncoding("UTF-8");
            assertThat(result.getContentAsString()).isEqualToIgnoringWhitespace(mapper.writeValueAsString(expectedResult));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFailedCreatePost() {
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

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testDeletePost() {
        try {
            var mockRequest = MockMvcRequestBuilders
                    .delete("http://localhost:8080/posts/0")
                    .accept(MediaType.APPLICATION_JSON);
            mockMvc.perform(mockRequest).andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
