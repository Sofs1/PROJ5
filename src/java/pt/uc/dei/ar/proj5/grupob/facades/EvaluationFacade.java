/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Evaluation;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;

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
                temp.setNote((double) student.getPaj().getScaleMin());
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

    /**
     * Submit a empty evaluation to that student when project is closed
     *
     * @param s Student
     */
    public void submitEmptyEvaluation(Student s) {
        for (Project p : s.getProjects()) {
            if (p.getEvaluations().isEmpty() && p.getEndDate().before(new Date())) {
                List<Evaluation> studentEvaluations = new ArrayList<>();
                for (Criteria c : s.getPaj().getCriteria()) {
                    Evaluation temp = new Evaluation();
                    temp.setStudent(s);
                    temp.setProject(p);
                    temp.setCriteria(c);
                    temp.setNote((double) s.getPaj().getScaleMin());
                    studentEvaluations.add(temp);
                }
                submitEvaluations(studentEvaluations);
            }
        }
    }

    /**
     * average from that criteria in that project
     *
     * @param list
     */
    public void averageCriteriaProj(List<Evaluation> list) {
        Query q = em.createNamedQuery("Evaluation.avgProjCrit");
        for (Evaluation e : list) {
            q.setParameter("id_proj", e.getProject().getId()).setParameter("id_crit", e.getCriteria().getId());
            try {
                e.setAverage((Double) q.getSingleResult());
            } catch (Exception ex) {
                Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     *
     * @param s Student
     * @param p Project
     * @return Evaluation List from that student on that project
     */
    public List<Evaluation> evaluationsStudentToProject(Student s, Project p) {
        Query q = em.createNamedQuery("Evaluation.findStudentProject");
        q.setParameter("id_st", s.getId()).setParameter("id_proj", p.getId());
        try {
            return (List<Evaluation>) q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * average from that project
     *
     * @param p Project
     * @return average value
     */
    public Double avgProject(Project p) {
        Query q = em.createNamedQuery("Evaluation.avgProj");
        q.setParameter("id_proj", p.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * project's average from that PAJ edition
     *
     * @param p Project
     * @return average value
     */
    public Double avgPaj(Paj p) {
        Query q = em.createNamedQuery("Evaluation.avgPaj");
        q.setParameter("id_paj", p.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * student's average from that PAJ edition
     *
     * @param s Student
     * @return average value
     */
    public Double avgStudent(Student s) {
        Query q = em.createNamedQuery("Evaluation.avgStud");
        q.setParameter("id_st", s.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * student's average from in that Project
     *
     * @param s Student
     * @param p Project
     * @return average value
     */
    public Double avgStudentProject(Student s, Project p) {
        Query q = em.createNamedQuery("Evaluation.avgStudProj");
        q.setParameter("id_st", s.getId()).setParameter("id_proj", p.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * student's average from in that Criterion
     *
     * @param s Student
     * @param c Criteria
     * @return average value
     */
    public Double avgStudentCriteria(Student s, Criteria c) {
        Query q = em.createNamedQuery("Evaluation.avgStudCrit");
        q.setParameter("id_st", s.getId()).setParameter("id_crit", c.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     *
     * @param s Student
     * @param c Criteria
     * @param p Project
     * @return average value
     */
    public Double noteStudentCriteriaProject(Student s, Criteria c, Project p) {
        Query q = em.createNamedQuery("Evaluation.avgStudProjCrit");
        q.setParameter("id_proj", p.getId()).setParameter("id_crit", c.getId()).setParameter("id_st", s.getId());
        try {
            return (Double) q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(EvaluationFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * verify if the list of Evaluations it´s with the minimum values
     *
     * @param studentEvaluations
     * @return boolean
     */
    public boolean verifyEvaluation(List<Evaluation> studentEvaluations) {
        int count = 0;
        for (Evaluation e : studentEvaluations) {
            if (e.getNote() == (double) e.getProject().getPaj().getScaleMin()) {
                count++;
            }
        }
        if (count == studentEvaluations.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the Average response of each user in each project
     *
     * @param p Paj
     * @return list of results
     */
    public List<Object[]> avgAdminAnsStudProj(Paj p) {
        Query q = em.createNamedQuery("Evaluation.avgAdminAnsStudProj");
        q.setParameter("paj", p);

        return (List<Object[]>) q.getResultList();
    }

    /**
     * Returns the Average response of a received Student per project
     *
     * @param s Student
     * @return list of results
     */
    public List<Object[]> avgAdminStdEachProj(Student s) {

        Query q = em.createNamedQuery("Evaluation.avgAdminStdEachProj");
        q.setParameter("student", s);

        return (List<Object[]>) q.getResultList();
    }

    /**
     * Average responses of each user for all projects undertaken by edition
     *
     * @param p Paj
     * @return list of results
     */
    public List<Object[]> avgAdminAnsAllStudByPaj(Paj p) {

        Query q = em.createNamedQuery("Evaluation.avgAdminAnsAllStudByPaj");
        q.setParameter("paj", p);

        return (List<Object[]>) q.getResultList();
    }

    public List<Object[]> avgAdminAllStudsEachAnsProj(Project p) {

        Query q = em.createNamedQuery("Evaluation.avgAdminAllStudsEachAnsProj");
        q.setParameter("proj", p);

        return (List<Object[]>) q.getResultList();

    }

    /**
     * Average of all users for each response in all projects
     *
     * @param p Paj
     * @return List<Object[]>
     */
    public List<Object[]> avgAdminAllStudsEachCrit(Paj p) {

        Query q = em.createNamedQuery("Evaluation.avgAdminAllStudsEachCrit");
        q.setParameter("paj", p);
        return (List<Object[]>) q.getResultList();
    }

    /**
     * Overall results between different editions
     *
     * @return List<Object[]>
     */
    public List<Object[]> avgAdminEachPajEdi() {

        Query q = em.createNamedQuery("Evaluation.avgAdminEachPajEdi");
        return (List<Object[]>) q.getResultList();

    }

    /**
     *
     * @param p Paj
     * @return list of results
     */
    public List<Object[]> avgAdminAllAnsByProj(Paj p) {

        Query q = em.createNamedQuery("Evaluation.avgAdminAllAnsByProj");
        q.setParameter("paj", p);
        return (List<Object[]>) q.getResultList();
    }

}
