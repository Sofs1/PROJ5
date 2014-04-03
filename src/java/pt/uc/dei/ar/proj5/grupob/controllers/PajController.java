/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;

/**
 *
 * @author sofia
 */
@Named
@RequestScoped
public class PajController {

    private Paj paj;

    public PajController() {
    }

    public Paj getPaj() {
        return paj;
    }

    public void setPaj(Paj paj) {
        this.paj = paj;
    }
    
    

    @PostConstruct
    public void initPajController() {
        this.paj = new Paj();
    }

}
