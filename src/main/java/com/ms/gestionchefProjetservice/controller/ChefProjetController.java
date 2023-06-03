package com.ms.gestionchefProjetservice.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ms.gestionchefProjetservice.classes.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ms.gestionchefProjetservice.entity.ChefProjet;
import com.ms.gestionchefProjetservice.service.ChefProjetService;

@RestController
@RequestMapping("/chef-projets")
public class ChefProjetController {
    
    @Autowired
    private ChefProjetService chefProjetService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/{id}")
    public ResponseEntity<ChefProjet> getChefProjetById(@PathVariable(name="id") Long id){

        ChefProjet chefProjet = null;
        HttpStatus status = HttpStatus.OK;

    try {
        chefProjet = this.chefProjetService.getChefProjetById(id);

        if (chefProjet == null || id == null) {
            status = HttpStatus.NOT_FOUND;
        }
    } catch (SQLException e) {
        e.printStackTrace();
        status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return new ResponseEntity<>(chefProjet, status);
    }


    @GetMapping
    public ChefProjet getChefProjetByEmail(@RequestParam("email") String email){
        try {
            return this.chefProjetService.getChefProjetByEmail(email);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping
    public ChefProjet ajoutChefProjet(@RequestBody ChefProjet chp){
        if(chp==null)
            return null;
        return this.chefProjetService.ajouterChefProjet(chp);
    }
    @PutMapping
    @ResponseBody
    public ResponseEntity<ChefProjet> modifierChefProjet(@RequestBody ChefProjet chefProjet) {
        System.out.println(chefProjet.getPhoto());
        ChefProjet chefProjetSaved = chefProjetService.ajouterChefProjet(chefProjet);

        byte[] photo = chefProjetSaved.getPhoto();
        String token = jwtTokenUtil.generateTokenChefProjet(chefProjetSaved.getEmail(), chefProjetSaved.getUsername(), chefProjetSaved.getId(),
                chefProjetSaved.getAdresse(), chefProjetSaved.getNom(), chefProjetSaved.getPrenom(), chefProjetSaved.getTelephone(),
                chefProjetSaved.getDateInscription(), "chefProjet", photo, chefProjetSaved.getPwd());


        if (chefProjetSaved == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(chefProjetSaved);

    }



}
