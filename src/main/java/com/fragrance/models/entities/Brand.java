package com.fragrance.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Table(name = "BRAND")
public class Brand implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;

    @NotEmpty
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "brand")
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
