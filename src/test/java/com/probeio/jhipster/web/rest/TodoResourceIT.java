package com.probeio.jhipster.web.rest;

import com.probeio.jhipster.JhipsterApp;
import com.probeio.jhipster.domain.Todo;
import com.probeio.jhipster.repository.TodoRepository;
import com.probeio.jhipster.service.TodoService;
import com.probeio.jhipster.service.dto.TodoDTO;
import com.probeio.jhipster.service.mapper.TodoMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

/**
 * Integration tests for the {@link TodoResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
@AutoConfigureWebTestClient
@WithMockUser
public class TodoResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private TodoService todoService;

    @Autowired
    private WebTestClient webTestClient;

    private Todo todo;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createEntity() {
        Todo todo = new Todo()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return todo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Todo createUpdatedEntity() {
        Todo todo = new Todo()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        return todo;
    }

    @BeforeEach
    public void initTest() {
        todoRepository.deleteAll().block();
        todo = createEntity();
    }

    @Test
    public void getAllTodos() {
        // Initialize the database
        todoRepository.save(todo).block();

        // Get all the todoList
        webTestClient.get().uri("/api/todos?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id").value(hasItem(todo.getId()))
            .jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION));
    }
    
    @Test
    public void getTodo() {
        // Initialize the database
        todoRepository.save(todo).block();

        // Get the todo
        webTestClient.get().uri("/api/todos/{id}", todo.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id").value(is(todo.getId()))
            .jsonPath("$.title").value(is(DEFAULT_TITLE))
            .jsonPath("$.description").value(is(DEFAULT_DESCRIPTION));
    }
    @Test
    public void getNonExistingTodo() {
        // Get the todo
        webTestClient.get().uri("/api/todos/{id}", Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound();
    }
}
