package com.example.usemanagement.service;

import com.example.usemanagement.entity.NewUser;
import com.example.usemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class NUserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    public NewUser loginUser(NewUser User){
        return userRepository.save(User);
    }
    public NewUser updateUser(NewUser User){
        return userRepository.save(User);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       NewUser newUser= findByUsername(username);
        return new org.springframework.security.core.userdetails.User(newUser.getUsername(),
                newUser.getPassword(),getAuthorities(newUser));
    }

    public NewUser findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
  private Set getAuthorities(NewUser newUser){
        Set authorities=new HashSet<>();
        newUser.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRolename()));
      });
      return authorities;
  }

}
