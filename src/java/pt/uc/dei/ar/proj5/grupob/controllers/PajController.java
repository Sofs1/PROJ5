/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.facades.CriteriaFacade;
import pt.uc.dei.ar.proj5.grupob.facades.PajFacade;
import pt.uc.dei.ar.proj5.grupob.util.PajDeleteException;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
@Named
@RequestScoped
public class PajController {

    private Paj paj;
    @Inject
    private PajFacade pajFacade;
    private Criteria criteria;
    @Inject
    private SessionController userEJB;
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

    public SessionController getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(SessionController userEJB) {
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

    /**
     * Create a new Edition Paj
     */
    public void createPaj() {
        pajFacade.create(paj);
        this.addMessage("PAJ Edition created successfuly");
    }

    /**
     *
     * @return boolean if exists Evaluations
     */
    public boolean existsEvaluations() {
        return pajFacade.exitsEvaluations(userEJB.getPajSelected());
    }

    /**
     * remove PAJ Edition
     */
    public void removepaj() {
        try {
            pajFacade.removePaj(paj);
            this.addMessage("PAJ Edition has been sucessfully removed");
        } catch (PajDeleteException ex) {
            Logger.getLogger(PajController.class.getName()).log(Level.SEVERE, null, ex);
            this.errorMessage(ex.getMessage());
        }
    }

    /**
     * create a new Criterion
     *
     * @return xhtml page - setCriteriaView
     */
    public String createCriteria() {
        criteriaFacade.createCriteria(criteria, userEJB.getPajSelected());
        criteria = null;
        return "setCriteriaView";
    }

    /**
     *
     * @param criteria
     * @return remove the selected criteria
     */
    public String removeCriteria(Criteria criteria) {
        criteriaFacade.removeCriteria(criteria, userEJB.getPajSelected());
        return "setCriteriaView";
    }

    /**
     * add a new message
     *
     * @param summary message
     */
    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    /**
     * add a new error message
     *
     * @param summary message
     */
    public void errorMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
