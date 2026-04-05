package ru.yandex.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.controller.CommentController;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.handler.GlobalExceptionHandler;
import ru.yandex.model.dto.CommentDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Test
    void getComments_shouldReturnList() throws Exception {
        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("text")
                .postId(1L)
                .build();

        when(commentService.getCommentsByPostId(1L))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/posts/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("text"));
    }

    @Test
    void getComment_shouldReturnOne() throws Exception {
        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("text")
                .postId(1L)
                .build();

        when(commentService.getComment(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/api/posts/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createComment_shouldReturnCreated() throws Exception {
        String request = """
        {
          "text": "hello"
        }
        """;

        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("hello")
                .postId(1L)
                .build();

        when(commentService.createComment(eq(1L), any()))
                .thenReturn(dto);

        mockMvc.perform(post("/api/posts/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateComment_shouldReturnUpdated() throws Exception {
        String request = """
        {
          "text": "updated"
        }
        """;

        CommentDto dto = CommentDto.builder()
                .id(1L)
                .text("updated")
                .postId(1L)
                .build();

        when(commentService.updateComment(eq(1L), eq(1L), any()))
                .thenReturn(dto);

        mockMvc.perform(put("/api/posts/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("updated"));
    }

    @Test
    void deleteComment_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/api/posts/1/comments/1"))
                .andExpect(status().isOk());

        verify(commentService).deleteComment(1L, 1L);
    }

    @Test
    void getComment_shouldReturn404_whenNotFound() throws Exception {
        when(commentService.getComment(1L, 1L))
                .thenThrow(new MyException(ExceptionType.COMMENT_NOT_FOUND, 1));

        mockMvc.perform(get("/api/posts/1/comments/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("COMMENT_NOT_FOUND"));
    }
}