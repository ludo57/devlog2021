package com.springjava.javaspring.security;

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
public class JwtRequestFilter extends OncePerRequestFilter {

    private UserDetailsServiceConfig userDetailsServiceConfig ;
    private JwtUtil jwtUtil;

    @Autowired
    public JwtRequestFilter(UserDetailsServiceConfig userDetailsServiceConfig, JwtUtil jwtUtil){
       this.userDetailsServiceConfig = userDetailsServiceConfig;
       this.jwtUtil =  jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader( "Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String jwt = authorizationHeader.substring(7);
            String login = jwtUtil.getTokenBody(jwt).getSubject();

            if(login != null && SecurityContextHolder.getContext().getAuthentication() == null){

                UserDetails userDetails = userDetailsServiceConfig.loadUserByUsername(login);

                if(jwtUtil.validToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }


    public UserDetailsServiceConfig getUserDetailsServiceConfig() {
        return userDetailsServiceConfig;
    }

    public void setUserDetailsServiceConfig(UserDetailsServiceConfig userDetailsServiceConfig) {
        this.userDetailsServiceConfig = userDetailsServiceConfig;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public void setJwtUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
}
