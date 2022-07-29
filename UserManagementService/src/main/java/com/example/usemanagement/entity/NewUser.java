package com.example.usemanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUser {
    @Id
    private long userid;
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",joinColumns = {
            @JoinColumn(name="userid")},
      inverseJoinColumns = {
            @JoinColumn(name="roleid")})
    private Set<Role> role;

}
