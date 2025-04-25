package org.example.cargo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class CrudServiceImpl<
        ID,
        E,    // Entity type
        R,    // Response DTO type
        C,    // Create DTO type
        U     // Update DTO type
        > implements CrudService<ID, R, C, U> {

    // The repository to interact with the database (JpaRepository)
    protected final JpaRepository<E, ID> repository;

    public CrudServiceImpl(JpaRepository<E, ID> repository) {
        this.repository = repository;
    }


    // Abstract mapping methods to be implemented in child service
    protected abstract E mapCreateDtoToEntity(C createDto);
    protected abstract E mapUpdateDtoToEntity(U updateDto, E existingEntity);
    protected abstract R mapEntityToResponseDto(E entity);

    @Override
    public R save(C createDto) {
        E entity = mapCreateDtoToEntity(createDto);
        E saved = repository.save(entity);
        return mapEntityToResponseDto(saved);
    }

    @Override
    public R update(U updateDto) {
        ID id = extractIdFromUpdateDto(updateDto);
        E existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entity not found with ID: " + id));
        E updated = mapUpdateDtoToEntity(updateDto, existing);
        E saved = repository.save(updated);
        return mapEntityToResponseDto(saved);
    }

    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }
    @Override
    public Optional<R> findById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with ID: " + id));
        return Optional.ofNullable(mapEntityToResponseDto(entity));
    }

//    @Override
//    public R findById(ID id) {
//        E entity = repository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("Entity not found with ID: " + id));
//        return mapEntityToResponseDto(entity);
//    }

    @Override
    public List<R> findAll() {
        return repository.findAll()
                .stream()
                .map(this::mapEntityToResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Extracts ID from the update DTO. Should be implemented in the child class.
     */
    protected abstract ID extractIdFromUpdateDto(U updateDto);
}
