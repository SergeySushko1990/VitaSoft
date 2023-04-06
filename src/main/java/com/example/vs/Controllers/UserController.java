package com.example.vs.Controllers;

import com.example.vs.Entity.Users;
import com.example.vs.Repository.UsersRepository;
import com.example.vs.Services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users/")
@AllArgsConstructor
public class UserController {

    private final UsersService usersService;
    private final UsersRepository usersRepository;

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> addUser(@RequestBody Users user){
        usersService.addUser(user);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PutMapping("/changerole")
    public ResponseEntity<HttpStatus> changeRole(@RequestParam(value = "id", required = true) long id){
        Optional<Users> user = usersRepository.findById(id);
        String role = user.get().getRole();
        System.out.println(role);
        if (role.equals("ROLE_USER")){
            usersService.changeRole(id);
            return ResponseEntity.ok(HttpStatus.OK);

        }
        else {
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
    }
}
