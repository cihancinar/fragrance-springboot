package com.fragrance.models.repositories;

import com.fragrance.models.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Optional<Model> findByProductIdAndId(Long productId, Long id);

    List<Model> findByProductId(Long productId);
}
