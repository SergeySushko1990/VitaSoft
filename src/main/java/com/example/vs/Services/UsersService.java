package com.example.vs.Services;

import com.example.vs.Entity.Ticket;
import com.example.vs.Entity.Users;
import com.example.vs.Repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    private final PasswordEncoder encoder;

    public String addUser(Users user){
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        user.setTime(new Timestamp(System.currentTimeMillis()));
        usersRepository.save(user);
        return "user added";
    }

    public long userIdInfo(Principal principal){
        Optional<Users> user = usersRepository.findByUsername(principal.getName());
        return user.get().getId();

    }

    public long findUserByName(String name){
        Optional<Users> user = usersRepository.findByUsernameContaining(name);
        return user.get().getId();
    }

    @Transactional
    public String changeRole(long id){
        return usersRepository.updateRole(id);
        }
}
