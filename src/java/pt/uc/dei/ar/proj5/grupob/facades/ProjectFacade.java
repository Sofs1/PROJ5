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
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;

/**
 *
 * @author sofia
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {
    
    @Inject
    private PajFacade pajFacade;
    
    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ProjectFacade() {
        super(Project.class);
    }
    
    public void addProject(Project p, Paj paj) {
        
        paj.getProjects().add(p);
        p.setPaj(paj);
        this.create(p);
        pajFacade.edit(paj);
    }
    
}
