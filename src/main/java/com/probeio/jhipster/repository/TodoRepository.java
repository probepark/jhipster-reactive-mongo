package com.probeio.jhipster.repository;

import com.probeio.jhipster.domain.Todo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Todo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TodoRepository extends ReactiveMongoRepository<Todo, String> {


    Flux<Todo> findAllBy(Pageable pageable);

}
