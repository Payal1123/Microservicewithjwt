package com.example.usemanagement.repository;

import com.example.usemanagement.entity.NewUser;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<NewUser,Long> {

      NewUser findByUsername(String username);
      //UserDetails findByUsername(String username);
}
