/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;

/**
 *
 * @author sofia
 */
@Stateless
public class CriteriaFacade extends AbstractFacade<Criteria> {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;
    @Inject
    private PajFacade pajFacade;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CriteriaFacade() {
        super(Criteria.class);
    }

    public void createCriteria(Criteria criteria, Paj paj) {
        paj.getCriteria().add(criteria);
        criteria.setPaj(paj);
        this.create(criteria);
        pajFacade.edit(paj);
    }
    
    public void removeCriteria(Criteria criteria, Paj paj) {
        paj.getCriteria().remove(criteria);
        this.remove(criteria);
        pajFacade.edit(paj);
    }

}
