package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.dto.UserDto.UserPartialRequestDto;
import com.TrendHive.TrendHive.dto.UserDto.UserRequestDto;
import com.TrendHive.TrendHive.dto.UserDto.UserResponseDto;
import com.TrendHive.TrendHive.entities.Merchant;
import com.TrendHive.TrendHive.entities.Role;
import com.TrendHive.TrendHive.entities.User;
import com.TrendHive.TrendHive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
//    private final RedisService redisService;
    private final RoleService roleService;
//    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository, EmailService emailService, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.emailService = emailService;

    }


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @PreAuthorize("permitAll()")
    public User create(User user){
        Optional<User> existingUser =userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST," User with username "+user.getUsername()+" is already exist!!....");
        }
        Role role = roleService.findByName("ROLE_USER");
        user.setRoles(Set.of(role));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPassword(encoder.encode(user.getPassword()));
//        user.setPassword(user.getPassword());
        emailService.sendEmail(user.getEmail(),"\uD83C\uDF89 Welcome to TrendHive – Your Shopping Journey Begins!","Hey "+user.getUsername()+",\n\nWelcome to TrendHive – the ultimate destination for trendy finds! \uD83D\uDECD\uFE0F✨\n" +
                "\nYour account is all set up, and you’re now part of a growing community of smart shoppers. \nexclusive deals, latest trends, and a seamless shopping experience.\n" +
                "\uD83D\uDD25 Start exploring now\n\nHappy shopping! \uD83D\uDC99\n\nTeam TrendHive");
        return userRepository.save(user);
    }

    public User createSuperUser(String username, String password, String email,String address){
        roleService.createAdminRole();

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User already exist.");
        }

        User user = new User();
        user.setUsername(username);
//        user.setPassword(passwordEncoder.encode(password));
//        user.setPassword(password);
        user.setPassword(encoder.encode(password));
        user.setEmail(email);
        user.setAddress(address);

        Role adminRole = roleService.findByName("ROLE_ADMIN");

        user.setRoles(Set.of(adminRole));

        return  userRepository.save(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<User> getAll(int page, int size, String sortDirection, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return userRepository.findAll(pageable);
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,""+
                "user with id "+id+" not found"));
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(int id){
        getById(id);
        userRepository.deleteById(id);
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
    public User updateById(int id , User user){
        User existingUser = getById(id);
        if (user.getUsername() != null){
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null){
            existingUser.setPassword(user.getPassword());
        }
        if (user.getEmail() != null){
            existingUser.setEmail(user.getEmail());
        }
        return  userRepository.save(existingUser);
    }

    public User convertToUser(UserRequestDto userRequestDto){
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setPassword(userRequestDto.getPassword());
        user.setEmail(userRequestDto.getEmail());
        user.setAddress(userRequestDto.getAddress());
//        user.setPhonenumber(userRequestDto.getPhonenumber());
        return user;
    }

    public UserResponseDto convertToUserResponseDto(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setUserName(user.getUsername());
        userResponseDto.setPassword(user.getPassword());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setCreatedDate(user.getCreatedDate());
        userResponseDto.setLastModifiedDate(user.getLastModifiedDate());
        userResponseDto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userResponseDto;
    }

    public User convertToUser(UserPartialRequestDto userPartialRequestDto){
        User user = new User();
        user.setUsername(userPartialRequestDto.getUsername());
        user.setPassword(userPartialRequestDto.getPassword());
        user.setEmail(userPartialRequestDto.getEmail());
        user.setAddress(userPartialRequestDto.getAddress());
//        user.setPhonenumber(userPartialRequestDto.getPhonenumber());
        return user;
    }
}
