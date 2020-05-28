package com.probeio.jhipster.web.rest;

import com.probeio.jhipster.service.TodoService;
import com.probeio.jhipster.web.rest.errors.BadRequestAlertException;
import com.probeio.jhipster.service.dto.TodoDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.reactive.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.probeio.jhipster.domain.Todo}.
 */
@RestController
@RequestMapping("/api")
public class TodoResource {

    private final Logger log = LoggerFactory.getLogger(TodoResource.class);

    private final TodoService todoService;

    public TodoResource(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * {@code GET  /todos} : get all the todos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of todos in body.
     */
    @GetMapping("/todos")
    public Mono<ResponseEntity<Flux<TodoDTO>>> getAllTodos(Pageable pageable, ServerHttpRequest request) {
        log.debug("REST request to get a page of Todos");
        return todoService.countAll()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page -> PaginationUtil.generatePaginationHttpHeaders(UriComponentsBuilder.fromHttpRequest(request), page))
            .map(headers -> ResponseEntity.ok().headers(headers).body(todoService.findAll(pageable)));
    }

    /**
     * {@code GET  /todos/:id} : get the "id" todo.
     *
     * @param id the id of the todoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the todoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/todos/{id}")
    public Mono<ResponseEntity<TodoDTO>> getTodo(@PathVariable String id) {
        log.debug("REST request to get Todo : {}", id);
        Mono<TodoDTO> todoDTO = todoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(todoDTO);
    }
}
