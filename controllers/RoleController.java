package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.dto.roledto.RoleRequestDto;
import com.TrendHive.TrendHive.entities.Role;
//import com.TrendHive.TrendHive.services.RoleService;
import com.TrendHive.TrendHive.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        return ResponseEntity.ok(roleService.createRole(role));
    }

    @PostMapping("/set")
    public ResponseEntity<Set<Role>> setRole(@RequestBody RoleRequestDto roleRequestDto){
        return ResponseEntity.ok(roleService.setRole(roleRequestDto));
    }

    @DeleteMapping("/user")
    public ResponseEntity<Void> deleteUserRole(@RequestBody RoleRequestDto roleRequestDto){
        roleService.removeRolesFromUser(roleRequestDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<Set<Role>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAlluserByRoleId());
    }
}
