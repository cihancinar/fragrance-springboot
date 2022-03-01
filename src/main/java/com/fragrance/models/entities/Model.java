package com.fragrance.models.entities;

import com.fragrance.models.entities.enums.Capacity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.Instant;
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
@Table(name = "MODEL")
public class Model implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;

    @NotEmpty
    private String name;
    private String description;
    private double price;
    private double capacityMl;
    private Capacity capacityName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

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
