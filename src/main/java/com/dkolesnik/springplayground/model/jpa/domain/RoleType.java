package com.dkolesnik.springplayground.model.jpa.domain;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Role Types Enumeration
 *
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-23
 */
public enum RoleType {

    /*
     * Admin
     */
    ADMIN(1L, "ADMIN"),

    /*
     * User
     */
    USER(2L, "USER");
    
    @Getter
    private final Long id;
    
    @Getter
    private final String roleName;

    public static final Map<Long, RoleType> ALL_ITEMS_MAP = Arrays.stream(RoleType.values()).collect(Collectors.toMap(RoleType::getId, itemType -> itemType));

    RoleType(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }
    
    @Override
    public String toString() {
        return this.roleName;
    }

    /**
     * Get Type entity by Id
     * 
     * @param id
     * @return RoleType
     */
    public static RoleType of(Long id) {
        if (id != null && ALL_ITEMS_MAP.containsKey(id)) {
            return ALL_ITEMS_MAP.get(id);
        }
        return null;
    }

    /**
     * Get Type entity by Name
     * 
     * @param name
     * @return RoleType
     */
    public static RoleType of(String name) {
        try {
            return RoleType.valueOf(name.toUpperCase());
        
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }

}
