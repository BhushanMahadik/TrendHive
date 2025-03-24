package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.dto.UserDto.UserPartialRequestDto;
import com.TrendHive.TrendHive.dto.UserDto.UserRequestDto;
import com.TrendHive.TrendHive.dto.UserDto.UserResponseDto;
import com.TrendHive.TrendHive.entities.User;
import com.TrendHive.TrendHive.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto){
        User user = userService.convertToUser(userRequestDto);
        User createdUser = userService.create(user);
        UserResponseDto userResponse = userService.convertToUserResponseDto(createdUser);
        return ResponseEntity.status(201).body(userResponse);
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ){
        Page<UserResponseDto> allUsers = userService.getAll(page, size, sortDirection, sortBy)
                .map(user -> userService.convertToUserResponseDto(user));
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable int id){
        User user = userService.getById(id);
        UserResponseDto userResponse = userService.convertToUserResponseDto(user);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserById(@PathVariable int id,@Valid @RequestBody UserRequestDto userRequestDto) {
        User user = userService.convertToUser(userRequestDto);
        User updatedUser = userService.updateById(id, user);
        UserResponseDto userResponse = userService.convertToUserResponseDto(updatedUser);
        return ResponseEntity.ok(userResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> UpdatePartialUserById(@PathVariable int id, @Valid @RequestBody UserPartialRequestDto userPartialRequestDto){
        User user = userService.convertToUser(userPartialRequestDto);
        User updatedUser = userService.updateById(id, user);
        UserResponseDto userResponse = userService.convertToUserResponseDto(updatedUser);
        return ResponseEntity.ok(userResponse);
    }
}
