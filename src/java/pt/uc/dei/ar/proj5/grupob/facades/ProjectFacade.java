/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.util.ExistEvaluationOnProjectException;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
@Stateless
public class ProjectFacade extends AbstractFacade<Project> {

    @Inject
    private StudentFacade studentFacade;

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectFacade() {
        super(Project.class);
    }

    public StudentFacade getStudentFacade() {
        return studentFacade;
    }

    public void setStudentFacade(StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    /**
     * adds the project p to paj
     *
     * @param p project
     * @param paj
     */
    public void addProject(Project p, Paj paj) {
        paj.getProjects().add(p);
        p.setPaj(paj);
        this.create(p);
        em.merge(paj);
    }

    /**
     * remove project from paj
     *
     * @param p Project
     * @param paj
     * @throws ExistEvaluationOnProjectException
     */
    public void removeProject(Project p, Paj paj) throws ExistEvaluationOnProjectException {
        if (!p.getEvaluations().isEmpty()) {
            throw new ExistEvaluationOnProjectException();
        } else {
            paj.getProjects().remove(p);
            this.remove(p);
            em.merge(paj);
        }

    }

    /**
     * adds a student list to a project
     *
     * @param st Student's list
     * @param p Project
     */
    public void addUsersToProject(List<Student> st, Project p) {

        for (Student s : st) {

            s.getProjects().add(p);
            em.merge(s);
            p.getStudents().add(s);
        }
        em.merge(p);

    }

    /**
     * Return all Projects in Data Base
     *
     * @return
     */
    public List<Project> allProjects() {

        Query q = em.createNamedQuery("Project.findAll");

        return (List<Project>) q.getResultList();
    }

}
