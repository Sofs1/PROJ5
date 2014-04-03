/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.facades.PajFacade;

/**
 *
 * @author sofia
 */
@Named
@RequestScoped
public class PajController {

    private Paj paj;
    @Inject
    private PajFacade pajFacade;

    public PajController() {
    }
    
//    @PostConstruct
//    public void initPajController() {
//        this.paj = new Paj();
//    }

    public Paj getPaj() {
        return paj;
    }

    public void setPaj(Paj paj) {
        this.paj = paj;
    }

    public PajFacade getPajFacade() {
        return pajFacade;
    }

    public void setPajFacade(PajFacade pajFacade) {
        this.pajFacade = pajFacade;
    }
    
    public String createPaj(){
        pajFacade.create(paj);
        return "adminLandingPage";
    }
    
    public void removepaj(){
        
    }
            
            
}
