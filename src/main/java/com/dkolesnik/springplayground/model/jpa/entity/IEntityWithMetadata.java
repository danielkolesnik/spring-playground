package com.dkolesnik.springplayground.model.jpa.entity;

import java.time.LocalDateTime;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-23
 */
public interface IEntityWithMetadata<T> extends IBaseEntity<T> {

    LocalDateTime getCreatedAt();
    
    LocalDateTime getUpdatedAt();
    
    Users getCreatedBy();
    
    Users getUpdatedBy();
    
}
