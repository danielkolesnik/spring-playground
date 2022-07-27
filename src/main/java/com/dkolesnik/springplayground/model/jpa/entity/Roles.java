package com.dkolesnik.springplayground.model.jpa.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-23
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString(of = {"id", "name"})
@EqualsAndHashCode(of = {"id"})
public class Roles implements IBaseEntity<Long> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<Users> users;
    
}
