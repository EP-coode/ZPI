package com.core.backend;

import com.core.backend.dto.comment.CommentCreateUpdateDto;
import com.core.backend.dto.comment.CommentDto;
import com.core.backend.exception.NoCommentException;
import com.core.backend.exception.NoIdException;
import com.core.backend.exception.NoPostException;
import com.core.backend.exception.WrongIdException;
import com.core.backend.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BackendCommentTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    PostService postService;
    @Test
    @Order(1)
    @WithAnonymousUser
    public void getComments() throws WrongIdException, NoIdException, NoPostException, NoCommentException {
        List<CommentDto> expectedResult = postService.getComments("-1");
        CommentDto expectedResult2 = postService.getCommentById("1");
        var mockRequest = MockMvcRequestBuilders
                .get("http://localhost:8080/posts/-1/comments")
                .accept(MediaType.APPLICATION_JSON);
        try {
            var response = mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn().getResponse();
            response.setCharacterEncoding("UTF-8");
            assertThat(response.getContentAsString()).isEqualToIgnoringWhitespace(mapper.writeValueAsString(expectedResult));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mockRequest = MockMvcRequestBuilders
                .get("http://localhost:8080/posts/-1/comments/1")
                .accept(MediaType.APPLICATION_JSON);
        try {
            var response = mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn().getResponse();
            assertThat(response.getContentAsString()).isEqualToIgnoringWhitespace(mapper.writeValueAsString(expectedResult2));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(2)
    @WithAnonymousUser
    public void createFailedComment() throws JsonProcessingException {
        createCommentHelper(status().isForbidden());
    }
    @Test
    @Order(3)
    @WithMockUser(username = "studentcommunityzpi@gmail.com", roles = "ADMIN")
    public void createComment() throws JsonProcessingException {
        createCommentHelper(status().isCreated());
    }

    @Test
    @Order(4)
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void likeComment() {
        var mockRequest = MockMvcRequestBuilders
                .get("http://localhost:8080/commentRating/like/5")
                .accept(MediaType.APPLICATION_JSON);
        try {
            mockMvc.perform(mockRequest).andExpect(status().isOk());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user@gmail.com", roles = "USER")
    public void deleteFailedComment() {
        deleteCommentHelper(status().isBadRequest());
    }
    @Test
    @Order(6)
    @WithMockUser(username = "studentcommunityzpi@gmail.com", roles = "ADMIN")
    public void deleteComment() {
        deleteCommentHelper(status().isOk());
    }



    private void deleteCommentHelper(ResultMatcher status) {
        var mockRequest = MockMvcRequestBuilders
                .delete("http://localhost:8080/posts/0/comments/5")
                .accept(MediaType.APPLICATION_JSON);
        try {
            mockMvc.perform(mockRequest).andExpect(status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createCommentHelper(ResultMatcher status) throws JsonProcessingException {
        CommentCreateUpdateDto comment = new CommentCreateUpdateDto("comment");
        var mockRequest = MockMvcRequestBuilders
                .post("http://localhost:8080/posts/0/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(comment));
        try {
            mockMvc.perform(mockRequest).andExpect(status);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
