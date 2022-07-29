package com.example.usemanagement.controller;
import com.example.usemanagement.entity.NewUser;
import com.example.usemanagement.entity.Role;
import com.example.usemanagement.jwtutils.JWTUtils;
import com.example.usemanagement.jwtutils.MediaTypeUtils;
import com.example.usemanagement.model.JwtRequest;
import com.example.usemanagement.model.JwtResponse;
import com.example.usemanagement.service.NUserService;
import com.example.usemanagement.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/login")
public class UserController {
    private static final String DIRECTORY = "D:/PDF";
    private static final String DEFAULT_FILE_NAME = "certificate.pdf";
    @Autowired
    NUserService nUserService;
    @Autowired
    RoleService roleService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ServletContext servletContext;
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("/user")
    public String loginUser(@RequestBody NewUser User) {
        NewUser newUser = nUserService.loginUser(User);
        return "User added successfully";
    }
    @PostMapping("/role")
    public String saveUserRole(@RequestBody Role role){
        roleService.saveUserRole(role);
        return "Role Save Successfully";
    }
    @PutMapping("/update")
    public NewUser updateUser(@RequestBody NewUser User){
        return nUserService.updateUser(User);
    }
    @GetMapping("/get")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InputStreamSource>getUserWithPdf(@RequestParam(defaultValue = DEFAULT_FILE_NAME)String fileName)
            throws IOException{
        ResponseEntity<String> requestEntity = restTemplate.getForEntity("http://localhost:8081/pdf/download",String.class);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
        File file = new File(DIRECTORY + "/" + fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                // Content-Disposition
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                // Content-Type
                .contentType(mediaType)
                // Contet-Length
                .contentLength(file.length()) //
                .body(resource);

    }
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('USER')")
    public NewUser getByUserName(@PathVariable String username){
        return nUserService.findByUsername(username);
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticateUser(@RequestBody JwtRequest jwtRequest) throws Exception {
        log.info(" Step 1 ");
        try{ authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                jwtRequest.getPassword()));
            log.info(" Step 2 ");
        }catch (BadCredentialsException e){
            log.info(" Step ggggg ");
            throw new Exception("Invalid Credential",e);
        }
        log.info(" Step 55 ");
        final UserDetails userDetails=nUserService.loadUserByUsername(jwtRequest.getUsername());
        log.info(" Step 55 ");
        final String token=jwtUtils.generateToken(userDetails);

        return new JwtResponse(token);
    }
}