/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.util.DuplicateEmailException;
import pt.uc.dei.ar.proj5.grupob.util.Encrypt;
import pt.uc.dei.ar.proj5.grupob.util.NotRegistedEmailException;
import pt.uc.dei.ar.proj5.grupob.util.PasswordException;

/**
 *
 * @author sofia
 */
@Stateless
public class StudentFacade extends AbstractFacade<Student> {

//    @Inject
//    private EvaluationFacade evaluationFacade;
//
//    @Inject
//    private ProjectFacade projectFacade;
//
//    @Inject
//    private LogFacade logFacade;
    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StudentFacade() {
        super(Student.class);
    }

    /**
     * search the user with the given email
     *
     * @param email
     * @return user
     */
    private User getStudentbyEmail(String email) {
        Query q = em.createNamedQuery("Student.findByEmail");
        q.setParameter("email", email);
        try {
            User user = (Student) q.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * create a new user (Student)
     *
     * @param user
     * @param passConf
     * @param paj
     * @throws PasswordException
     * @throws DuplicateEmailException
     */
    public void createStudent(Student user, String passConf, Paj paj) throws PasswordException, DuplicateEmailException {
        if (getStudentbyEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException();
        } else if (!user.getPass().equals(passConf)) {
            throw new PasswordException();
        } else {
            String passEncripted = Encrypt.cryptWithMD5(user.getPass());
            user.setPass(passEncripted);
            create(user);
            user.setPaj(paj);
            paj.getStudents().add(user);
            edit(user);
            em.merge(paj);

        }
    }

    /**
     * verify if exists user with that email e password
     *
     * @param email
     * @param password
     * @return user
     * @throws NotRegistedEmailException
     * @throws PasswordException
     */
    public User searchStudent(String email, String password) throws NotRegistedEmailException, PasswordException {
        User studTemp = getStudentbyEmail(email);
        String passEncripted = Encrypt.cryptWithMD5(password);
        if (studTemp == null) {
            throw new NotRegistedEmailException();
        } else if (!studTemp.getPass().equals(passEncripted)) {
            throw new PasswordException();
        } else {
            return studTemp;
        }
    }

    /**
     * edit user
     *
     * @param user
     * @param passConf
     * @param email
     * @throws PasswordException
     * @throws DuplicateEmailException
     */
    public void editStudentFacade(User user, String passConf, String email) throws PasswordException, DuplicateEmailException {
        User studTemp = getStudentbyEmail(email);
        if (!user.getPass().equals(passConf)) {
            throw new PasswordException();
        } else {
            if (studTemp == null || studTemp.getId().equals(user.getId())) {
                String passEncripted = Encrypt.cryptWithMD5(user.getPass());
                user.setPass(passEncripted);
                getEntityManager().merge(user);
            } else {
                throw new DuplicateEmailException();
            }
        }
    }

    /**
     * first remove all the Student traces and then remove the Student
     *
     * @param student
     * @param paj
     */
    public void deleteStudent(Student student, Paj paj) {
        remove(em.merge(student));
// int idStd = student.getId();
//        // remove from Students list of Paj that student
//        for (Student s : paj.getStudents()) {
//            if (s.getId() == idStd) {
//                paj.getStudents().remove(s);
//            }
//        }
//        getEntityManager().merge(paj);
//
//        // remove from Evaluation BD the evaluations from that Student
//        for (Evaluation e : evaluationFacade.findAll()) {
//            if (e.getStudent().getId() == idStd) {
//                getEntityManager().remove(getEntityManager().merge(e));
//            }
//        }
//        //remove from all projects that student
//        for (Project p : projectFacade.findAll()) {
//            for (int i = 0; i < p.getStudents().size(); i++) {
//                Student temp = p.getStudents().get(i);
//                if (temp.getId() == idStd) {
//                    getEntityManager().remove(getEntityManager().merge(temp));
//                }
//            }
//        }
//        //remove student from Log's table
//        for (Log l : logFacade.findAll()) {
//            if (l.getStudent().getId() == idStd) {
//                getEntityManager().remove(l);
//            }
//        }
    }

    public List<Project> openProjects(Student s) {
        List<Project> temp = new ArrayList<>();
        for (Project p : s.getProjects()) {
            if (!p.getBegDate().before(new Date()) && !p.getEndDate().after(new Date())) {
                temp.add(p);
            }
        }
        return temp;
    }

    public List<Student> listStudentsPaj(Paj paj) {
        Query q = em.createNamedQuery("Student.findByPaj");
        q.setParameter("paj", paj);
        try {
            return (List<Student>) q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Student> getSearchByName(String str, Paj paj) {
        Query q = em.createNamedQuery("Student.searchStudents");
        q.setParameter("name", "%" + str + "%").setParameter("paj", paj);
        try {
            return (List<Student>) q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
