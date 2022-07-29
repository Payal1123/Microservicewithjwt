package com.example.usemanagement.filter;

import com.example.usemanagement.jwtutils.JWTUtils;
import com.example.usemanagement.service.NUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    JWTUtils jwtUtils;
    @Autowired
    NUserService nUserService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
      String TokenHeader=request.getHeader("Authorization");
      String token=null;
      String userName=null;
      if(TokenHeader!=null && TokenHeader.startsWith("Bearer ")) {
          token = TokenHeader.substring(7);
          userName = jwtUtils.getUsernameFromToken(token);
      }
          if (userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails= nUserService.loadUserByUsername(userName);

            if (jwtUtils.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().
                        buildDetails(request));
                SecurityContextHolder
                        .getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response );
    }
}
