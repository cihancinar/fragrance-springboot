package com.fragrance.models.repositories;

import com.fragrance.models.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
}
