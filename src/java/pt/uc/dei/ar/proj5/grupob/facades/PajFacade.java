/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pt.uc.dei.ar.proj5.grupob.entities.Log;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.util.PajDeleteException;

/**
 *
 * @author sofia
 */
@Stateless
public class PajFacade extends AbstractFacade<Paj> {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PajFacade() {
        super(Paj.class);
    }

    public boolean exitsEvaluations(Paj paj) {
        for (Project p : paj.getProjects()) {
            if (!p.getEvaluations().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void removePaj(Paj paj) throws PajDeleteException {
        if (!exitsEvaluations(paj)) {
            remove(em.merge(paj));
        } else {
            throw new PajDeleteException();
        }

    }

}
