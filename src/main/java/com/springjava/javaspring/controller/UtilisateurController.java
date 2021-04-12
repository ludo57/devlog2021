package com.springjava.javaspring.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.springjava.javaspring.dao.UtilisateurDao;
import com.springjava.javaspring.model.Utilisateur;
import com.springjava.javaspring.security.JwtUtil;
import com.springjava.javaspring.security.UserDetailsCustom;
import com.springjava.javaspring.security.UserDetailsServiceConfig;
import com.springjava.javaspring.view.JsonViewCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class UtilisateurController {

    UtilisateurDao utilisateurDao;
    JwtUtil jwtUtil;
    AuthenticationManager authenticationManager;
    UserDetailsServiceConfig userDetailsServiceConfig;

     @Autowired
    UtilisateurController(UtilisateurDao utilisateurDao, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          UserDetailsServiceConfig userDetailsServiceConfig){
         this.utilisateurDao = utilisateurDao;
         this.jwtUtil = jwtUtil;
         this.authenticationManager = authenticationManager;
         this.userDetailsServiceConfig = userDetailsServiceConfig;
     }


    @PostMapping("/authentification")
    public String authentification(@RequestBody Utilisateur utilisateur){

         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         utilisateur.getLogin(), utilisateur.getPassword()
                 )
         );


        UserDetails userDetails = this.userDetailsServiceConfig.loadUserByUsername(utilisateur.getLogin());


        return jwtUtil.generateToken(userDetails);

    }


    @JsonView(JsonViewCustom.VueUtilisateur.class)
    @GetMapping("/user/utilisateur/{id}")
    public Utilisateur getUtilisateurs(@PathVariable int id){
         Utilisateur utilisateur = utilisateurDao.findById(id).orElse(null);
        return utilisateurDao.findById(id).orElse(null);
    }


    @JsonView(JsonViewCustom.VueUtilisateur.class)
    @GetMapping("user/utilisateurs")
    public List<Utilisateur> getUtilisateurs(){

        return utilisateurDao.findAll();
    }

    @PostMapping("/utilisateur")
    public boolean addUtilisateur (@RequestBody Utilisateur utilisateur) {

         utilisateurDao.save(utilisateur);

        return true;
    }

    @DeleteMapping("/utilisateur/{id}")
    public boolean deleteUtilisateur (@PathVariable int id) {

         utilisateurDao.deleteById(id);
        return true;
    }


}

