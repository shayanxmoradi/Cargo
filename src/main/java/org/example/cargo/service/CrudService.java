package org.example.cargo.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
/**
 * Generic CRUD (Create, Read, Update, Delete) service interface for handling business logic
 * with different types of DTOs for each operation.
 */
public interface CrudService<ID, R, C, U> {
    R save(C createDto);

    R update(U updateDto);

    void deleteById(ID id);

    Optional<R> findById(ID id);

    List<R> findAll();
 Page<R> findAll(Pageable pageable);//todo care

}
