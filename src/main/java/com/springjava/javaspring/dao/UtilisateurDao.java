package com.springjava.javaspring.dao;

import com.springjava.javaspring.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurDao extends JpaRepository<Utilisateur,Integer> {

   Optional<Utilisateur> findByLogin(String login);
}
