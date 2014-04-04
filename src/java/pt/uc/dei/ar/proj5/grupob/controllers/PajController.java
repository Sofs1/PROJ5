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
import pt.uc.dei.ar.proj5.grupob.ejbs.UserEJB;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.facades.CriteriaFacade;
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
    private Criteria criteria;
    @Inject
    private UserEJB userEJB;
    @Inject
    private CriteriaFacade criteriaFacade;

    public PajController() {
    }

    @PostConstruct
    public void initPajController() {
        this.paj = new Paj();
        this.criteria = new Criteria();
    }

    public CriteriaFacade getCriteriaFacade() {
        return criteriaFacade;
    }

    public void setCriteriaFacade(CriteriaFacade criteriaFacade) {
        this.criteriaFacade = criteriaFacade;
    }

    public UserEJB getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(UserEJB userEJB) {
        this.userEJB = userEJB;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

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

    public String createPaj() {
        pajFacade.create(paj);
        return "adminLandingPage";
    }

    public boolean existsEvaluations() {
        return pajFacade.exitsEvaluations(userEJB.getPajSelected());
    }

    public void removepaj() {

    }

    public String createCriteria() {
        criteriaFacade.createCriteria(criteria, userEJB.getPajSelected());
        criteria = null;
        return "setCriteriaView";
    }

    public String removeCriteria(Criteria criteria) {
        criteriaFacade.removeCriteria(criteria, userEJB.getPajSelected());
        return "setCriteriaView";
    }

}
