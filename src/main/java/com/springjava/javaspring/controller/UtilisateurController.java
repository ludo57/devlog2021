package com.springjava.javaspring.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.springjava.javaspring.dao.UtilisateurDao;
import com.springjava.javaspring.model.Role;
import com.springjava.javaspring.model.Utilisateur;
import com.springjava.javaspring.security.JwtUtil;
import com.springjava.javaspring.security.UserDetailsCustom;
import com.springjava.javaspring.security.UserDetailsServiceConfig;
import com.springjava.javaspring.view.JsonViewCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class UtilisateurController {

     UtilisateurDao utilisateurDao;
     JwtUtil jwtUtil;
     AuthenticationManager authenticationManager;
     UserDetailsServiceConfig userDetailsServiceConfig;
     PasswordEncoder passwordEncoder;

     @Autowired
    UtilisateurController(UtilisateurDao utilisateurDao, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager,
                          UserDetailsServiceConfig userDetailsServiceConfig,
                          PasswordEncoder passwordEncoder){
         this.utilisateurDao = utilisateurDao;
         this.jwtUtil = jwtUtil;
         this.authenticationManager = authenticationManager;
         this.userDetailsServiceConfig = userDetailsServiceConfig;
         this.passwordEncoder = passwordEncoder;
     }


    @PostMapping("/authentification")
    public ResponseEntity<String> authentification(@RequestBody Utilisateur utilisateur){

         try{
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(
                             utilisateur.getLogin(),utilisateur.getPassword()
                     )
             );
         }catch(AuthenticationException e){
             return ResponseEntity.badRequest().body("Mauvais login ou mot de passe");
         }

         authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(
                         utilisateur.getLogin(), utilisateur.getPassword()
                 )
         );


        UserDetails userDetails = this.userDetailsServiceConfig.loadUserByUsername(utilisateur.getLogin());


        return ResponseEntity.ok(jwtUtil.generateToken(userDetails));

    }

    @PostMapping("/inscription")
    public ResponseEntity<String> inscription(@RequestBody Utilisateur utilisateur){

        Optional<Utilisateur> utilisateurDoublon = utilisateurDao.findByLogin(utilisateur.getLogin());

        if(utilisateurDoublon.isPresent()) {
            return ResponseEntity.badRequest().body("Ce login est déja utilisé");
        } else {

            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));

            Role roleUtilisateur = new Role();
            roleUtilisateur.setId(2);

            utilisateur.getListeRole().add(roleUtilisateur);

            utilisateurDao.saveAndFlush(utilisateur);

            return ResponseEntity.ok(Integer.toString(utilisateur.getId()));
        }
    }

    @JsonView(JsonViewCustom.VueUtilisateur.class)
    @GetMapping("/user/utilisateur/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurs(@PathVariable int id){

         Optional<Utilisateur> utilisateur = utilisateurDao.findById(id);

         if(utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }


    @JsonView(JsonViewCustom.VueUtilisateur.class)
    @GetMapping("user/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getUtilisateurs(){

        return ResponseEntity.ok(utilisateurDao.findAll());
    }

    /*@PostMapping("/utilisateur")
    public boolean addUtilisateur (@RequestBody Utilisateur utilisateur) {

         utilisateurDao.save(utilisateur);

        return true;
    }*/

    @DeleteMapping("/admin/utilisateur/{id}")
    public ResponseEntity<Integer> deleteUtilisateur (@PathVariable int id) {

         if(utilisateurDao.existsById(id)){
             utilisateurDao.deleteById(id);
             return ResponseEntity.ok().body(id);
         }else{
             return ResponseEntity.noContent().build();
         }

    }


}

