package com.dkolesnik.springplayground.security;

import com.dkolesnik.springplayground.model.jpa.entity.Users;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Description
 *
 * @author Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version 0.1.1
 * @since 2022-07-26
 */
public class UserDetailsImpl extends User {
    
    private static final Long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    
    @Getter
    private Long userId;

    public UserDetailsImpl(Long userId, String username, String password, boolean enabled, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, true, true, true, authorities);
        
        this.userId = userId;
    }
    
    public UserDetailsImpl(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities, Long userId) {
        this(userId, userDetails.getUsername(), userDetails.getPassword(), userDetails.isEnabled(), authorities);
    }
    
    public UserDetailsImpl(Users user) {
        this(user.getId(), user.getEmail(), user.getPassword(), user.getIsEnabled(), AuthorityUtils.NO_AUTHORITIES);
    }
    
    public static UserDetailsImpl of(Users user) {
        List<GrantedAuthority> authorities = user.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
        
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), Optional.ofNullable(user.getPassword()).orElse(""), user.getIsEnabled(), authorities);
        
        return userDetails;
    }
}
