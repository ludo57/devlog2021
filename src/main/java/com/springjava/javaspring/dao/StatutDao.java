package com.springjava.javaspring.dao;

import com.springjava.javaspring.model.Statut;
import com.springjava.javaspring.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutDao extends JpaRepository<Statut,Integer> {


}
