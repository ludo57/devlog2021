package com.springjava.javaspring.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.springjava.javaspring.dao.CompetenceDao;
import com.springjava.javaspring.model.Competence;
import com.springjava.javaspring.view.JsonViewCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin

public class CompetenceController {

    CompetenceDao competenceDao;

     @Autowired
     CompetenceController(CompetenceDao competenceDao){
         this.competenceDao = competenceDao;
    }

    @GetMapping("/competence/{id}")
    @JsonView({JsonViewCustom.VueCompetence.class})
    public Competence getCompetences(@PathVariable int id){

        return competenceDao.findById(id).orElse(null);
    }

    @GetMapping("/competences")
    @JsonView({JsonViewCustom.VueCompetence.class})
    public List<Competence> getCompetences(){

        return competenceDao.findAll();
    }


    @PostMapping("/competence")
    public boolean addCompetence (@RequestBody Competence competence) {

         competenceDao.save(competence);

        return true;
    }

    @DeleteMapping("/competence/{id}")
    public boolean deleteCompetence (@PathVariable int id) {

         competenceDao.deleteById(id);
        return true;
    }


}

