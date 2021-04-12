package com.springjava.javaspring.security;

import com.springjava.javaspring.dao.UtilisateurDao;
import com.springjava.javaspring.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



    @Service
  public class UserDetailsServiceConfig implements UserDetailsService {


        @Autowired
       UtilisateurDao utilisateurDao;

        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException{
            Utilisateur utilisateur =  utilisateurDao.findByLogin(s);

            return new UserDetailsCustom(utilisateur);

        }

    }





