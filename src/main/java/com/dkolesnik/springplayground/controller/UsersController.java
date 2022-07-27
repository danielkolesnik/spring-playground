package com.dkolesnik.springplayground.controller;

import com.dkolesnik.springplayground.model.jpa.entity.Users;
import com.dkolesnik.springplayground.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Users Rest Controller
 *
 * @author   Daniel A. Kolesnik <daneelkolesnik@gmail.com>
 * @version  0.1.1
 * @since    2022-07-17
 */
@RestController
@RequestMapping(
    value = UsersController.CONTROLLER_URI,
    produces = MediaType.APPLICATION_JSON,
    name = "Users Management Controller"
)
public class UsersController {
    
    static final String CONTROLLER_URI = "/api/users";
    
    private final UsersService usersService;
    
    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    /**
     * Get Users List 
     * 
     * @param name
     * @return Users List
     */
    @RequestMapping(method = RequestMethod.GET, value = "/list", name = "Get Users List")
    public List<Users> getList(@RequestParam(value = "name", defaultValue = "") String name) {
        List<Users> result = usersService.getList(name);
        
        return result;
    }

    /**
     * Get User Details
     * 
     * @param itemId
     * @return User Details
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{itemId}", name = "Get User Details")
    public Users getItem(@PathVariable("itemId") @NotNull @Size(min = 1) Long itemId) {
        Users result = usersService.getItem(itemId);
        
        return result;
    }
    
}
