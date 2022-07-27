package com.dkolesnik.springplayground.service;

import com.dkolesnik.springplayground.model.jpa.entity.Users;
import com.dkolesnik.springplayground.repository.jpa.RolesRepository;
import com.dkolesnik.springplayground.repository.jpa.UsersRepository;
import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotFoundException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

/**
 * Users CRUD Service
 *
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-18
 */
@Service
@Slf4j
public class UsersService {
    
    private final UsersRepository usersRepository;
    
    private final RolesRepository rolesRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UsersService(UsersRepository usersRepository, RolesRepository rolesRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.rolesRepository = rolesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get Users List
     * 
     * @param name
     * @return Users List
     */
    public List<Users> getList(String name) {
        String namePattern = "";
        
        if (StringUtils.isNotEmpty(name)) {
            namePattern = name;
        }
        namePattern += "%";
        
        List<Users> result = usersRepository.findAllByFirstNameIsLike(namePattern);
        
        return result;
    }

    /**
     * Get Users Details
     * 
     * @param itemId
     * @return User Details
     */
    public Users getItem(Long itemId) {
        Optional<Users> itemDetailsOpt = usersRepository.findById(itemId);
        
        if (itemDetailsOpt.isEmpty()) {
            throw new NotFoundException(MessageFormat.format("User [{0}] not found in database!", itemId));
        }
        
        return itemDetailsOpt.get();
    }
    
}
