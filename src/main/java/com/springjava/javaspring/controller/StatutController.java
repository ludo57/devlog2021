package com.springjava.javaspring.controller;

import com.springjava.javaspring.dao.StatutDao;
import com.springjava.javaspring.model.Statut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@CrossOrigin
public class StatutController {

    StatutDao statutDao;

     @Autowired
     StatutController(StatutDao statutDao){
         this.statutDao = statutDao;
    }

    @GetMapping("/admin/user/statut/{id}")
    public Statut getStatuts(@PathVariable int id){

        return statutDao.findById(id).orElse(null);
    }

    @GetMapping("/admin/user/statuts")
    public List<Statut> getStatuts(){

      List<Statut> listStatut = statutDao.findAll() ;

        return listStatut;
    }

    @PostMapping("/admin/user/statut")
    public boolean addStatut (@RequestBody Statut statut) {

         statutDao.save(statut);

        return true;
    }

    @DeleteMapping("/statut/{id}")
    public boolean deleteStatut (@PathVariable int id) {

         statutDao.deleteById(id);
        return true;
    }


}

