/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
@Stateless
public class CriteriaFacade extends AbstractFacade<Criteria> {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CriteriaFacade() {
        super(Criteria.class);
    }

    /**
     * Create a criteria and persist to a BD
     *
     * @param criteria
     * @param paj
     */
    public void createCriteria(Criteria criteria, Paj paj) {
        paj.getCriteria().add(criteria);
        criteria.setPaj(paj);
        this.create(criteria);
        em.merge(paj);
    }

    /**
     * remove the criteria from paj edition
     *
     * @param criteria
     * @param paj
     */
    public void removeCriteria(Criteria criteria, Paj paj) {
        paj.getCriteria().remove(criteria);
        this.remove(criteria);
        em.merge(paj);
    }

}
