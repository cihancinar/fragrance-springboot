package com.fragrance.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class TechnicalDto implements Serializable {
    Long id;
    UUID ref;
    Instant created;
    Instant updated;
    String updatedBy;
}
