/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Evaluation;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.util.AlreadyExistsYourEvaluation;
import pt.uc.dei.ar.proj5.grupob.util.NoEntriesToEvaluation;

/**
 *
 * @author sofia
 */
@Stateless
public class EvaluationFacade extends AbstractFacade<Evaluation> {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluationFacade() {
        super(Evaluation.class);
    }

    /**
     * Create a list of evaluations from that student to a defined project
     *
     * @param student
     * @param p (project)
     * @return Evaluation's list
     */
    public List<Evaluation> studentEvaluationsSetCriteria(Student student, Project p) {
        if (evaluationsStudentToProject(student, p).isEmpty()) {
            List<Evaluation> studentEvaluations = new ArrayList<>();
            for (Criteria c : student.getPaj().getCriteria()) {
                Evaluation temp = new Evaluation();
                temp.setStudent(student);
                temp.setProject(p);
                temp.setCriteria(c);
                temp.setNote(0.0);
                studentEvaluations.add(temp);
            }
            return studentEvaluations;
        } else {
            return evaluationsStudentToProject(student, p);
        }
    }

    /**
     * Persit the List of Evaluations from 1student about 1project
     *
     * @param list
     */
    public void submitEvaluations(List<Evaluation> list) {
        for (Evaluation e : list) {
            create(e);
            e.getProject().getEvaluations().add(e);
            em.merge(e.getProject());
            e.getStudent().getEvaluations().add(e);
            em.merge(e.getStudent());
            e.getCriteria().getEvaluations().add(e);
            em.merge(e.getCriteria());
        }
    }

    public List<Evaluation> evaluationsStudentToProject(Student s, Project p) {
        Query q = em.createNamedQuery("Evaluation.findStudentProject");
        q.setParameter("id_st", s.getId()).setParameter("id_proj", p.getId());
        try {
            return (List<Evaluation>) q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public void giveEvaluation(List<Evaluation> studentEvaluations) throws NoEntriesToEvaluation {
        int count = 0;
        for (Evaluation e : studentEvaluations) {
            if (e.getNote() == 0.0) {
                count++;
            }
        }
        if (count == studentEvaluations.size()) {
            throw new NoEntriesToEvaluation();
        } else {
            submitEvaluations(studentEvaluations);
        }
    }

}
