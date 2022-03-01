package com.fragrance.models.entities;

import com.fragrance.models.entities.enums.Gender;
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
@Table(name = "PRODUCT")
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID ref;

    @NotEmpty
    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_ingredients",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    private Set<Ingredient> ingredients = new HashSet<>();
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "product")
    private Set<Model> models = new HashSet<>();

    @ElementCollection
    private Set<String> tags = new HashSet<>();
    @ElementCollection
    private Set<Gender> genders = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id")
    )
    private Set<Category> categories = new HashSet<>();

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
