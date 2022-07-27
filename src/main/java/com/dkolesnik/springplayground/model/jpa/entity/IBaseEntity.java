package com.dkolesnik.springplayground.model.jpa.entity;

/**
 * Base Entity interface definition
 *
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-23
 */
public interface IBaseEntity<T> {

    T getId();

}
