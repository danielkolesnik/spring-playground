package com.dkolesnik.springplayground.repository.jpa;

import com.dkolesnik.springplayground.model.jpa.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-17
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    List<Users> findAllByFirstNameIsLike(String firstName);
    
    List<Users> findAllByEmailIgnoreCase(String email);
    
    Optional<Users> findByEmail(String email);
    
}
