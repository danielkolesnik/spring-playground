package com.dkolesnik.springplayground.repository.jpa;

import com.dkolesnik.springplayground.model.jpa.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-23
 */
@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    
    Roles findByName(String name);
    
    @Query(value = "SELECT r.* FROM roles r INNER JOIN user_roles ur ON ur.role_id=r.id " +
        "INNER JOIN users u ON ur.user_id = u.id WHERE UPPER(u.email) = UPPER(:email)", nativeQuery = true)
    List<Roles> findAllByUserEmailIgnoreCase(@Param("email") String email);
    
}
