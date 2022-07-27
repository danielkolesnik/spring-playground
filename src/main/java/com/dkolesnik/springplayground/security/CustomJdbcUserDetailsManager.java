package com.dkolesnik.springplayground.security;

import com.dkolesnik.springplayground.repository.jpa.RolesRepository;
import com.dkolesnik.springplayground.repository.jpa.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private RolesRepository rolesRepository;
    
    @Autowired
    private DataSource dataSource;
    
    @PostConstruct
    public void setup() {
        super.setDataSource(dataSource);
    }
    
    @Override
    public List<UserDetails> loadUsersByUsername(final String username) {
        List<UserDetails> result = usersRepository.findAllByEmailIgnoreCase(username)
            .stream()
            .map(UserDetailsImpl::new)
            .collect(Collectors.toList());
        
        return result;
    }
    
    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        List<GrantedAuthority> result = rolesRepository.findAllByUserEmailIgnoreCase(username)
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        
        return result;
    }
    
    @Override
    protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
        UserDetailsImpl result = new UserDetailsImpl(userFromUserQuery, combinedAuthorities, ((UserDetailsImpl) userFromUserQuery).getUserId());
        
        return result;
    }
    
}
