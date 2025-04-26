package org.example.cargo.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity<ID extends Serializable> {
    public static final String ID = "id";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ID)
    protected ID id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
