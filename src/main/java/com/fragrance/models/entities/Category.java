package com.fragrance.models.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author ccinar
 * @created 14/02/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CATEGORY")
@EqualsAndHashCode(exclude = {"products"})
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;

    @NotEmpty
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    @Lob
    private byte[] image;

    private Instant created;
    private Instant updated;
    private String updatedBy;

    @PrePersist
    private void prePersist() {
        ref = UUID.randomUUID();
        created = Instant.now();
        updated = created;
    }

    @PreUpdate
    private void preUpdate() {
        updated = Instant.now();
    }
}
