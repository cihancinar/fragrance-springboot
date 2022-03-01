package com.fragrance.models.repositories;

import com.fragrance.models.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
