package com.core.backend.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/role")
public class RoleController {

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping(value = "/{roleName}")
    public ResponseEntity<Object> getRole(@PathVariable("roleName") String roleName) {
        if (roleName == null)
            return new ResponseEntity<>("Brak wartości dla pola roleName", HttpStatus.BAD_REQUEST);
        Optional<Role> roleOptional = roleRepository.findById(roleName);
        if (roleOptional.isPresent())
            return new ResponseEntity<>(roleOptional.get(), HttpStatus.OK);
        return new ResponseEntity<>("Brak roli o podanym ID", HttpStatus.NOT_FOUND);
    }
}
