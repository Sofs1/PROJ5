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
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.util.ExistEvaluationOnProjectException;

/**
 *
 * @author sofia
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

    public void addProject(Project p, Paj paj) {
        paj.getProjects().add(p);
        p.setPaj(paj);
        this.create(p);
        em.merge(paj);
    }

    public void removeProject(Project p, Paj paj) throws ExistEvaluationOnProjectException {
        if (!p.getEvaluations().isEmpty()) {
            throw new ExistEvaluationOnProjectException();
        } else {
            paj.getProjects().remove(p);
            this.remove(p);
            em.merge(paj);
        }

    }

    public void addUsersToProject(List<Student> st, Project p) {

        for (Student s : st) {

            s.getProjects().add(p);
            em.merge(s);
            p.getStudents().add(s);

        }
        em.merge(p);
//        p.getStudents().addAll(st);
//        em.persist(p);
    }

}
