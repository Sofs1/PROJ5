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
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Evaluation;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
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

    @Inject
    private EvaluationFacade evaluationFacade;

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
     * @throws PasswordException
     * @throws DuplicateEmailException
     */
    public void createStudent(Student user, String passConf) throws PasswordException, DuplicateEmailException {
        if (getStudentbyEmail(user.getEmail()) != null) {
            throw new DuplicateEmailException();
        } else if (!user.getPass().equals(passConf)) {
            throw new PasswordException();
        } else {
            String passEncripted = Encrypt.cryptWithMD5(user.getPass());
            user.setPass(passEncripted);
            getEntityManager().persist(user);
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
        String passEncripted = Encrypt.cryptWithMD5(studTemp.getPass());
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
            if (studTemp == null || studTemp.getId() == user.getId()) {
                String passEncripted = Encrypt.cryptWithMD5(user.getPass());
                user.setPass(passEncripted);
                getEntityManager().merge(user);
            } else {
                throw new DuplicateEmailException();
            }
        }
    }

    /**
     *
     * @param student
     * @param paj
     */
    public void deleteStudent(Student student, Paj paj) {
        for (Student s : paj.getStudents()) {
            if (s.getId() == student.getId()) {
                paj.getStudents().remove(s);
            }
        }
        Query q = em.createNamedQuery("Evaluation.findStudent");
        q.setParameter("id", student.getId());
        for (Evaluation e : evaluationFacade.findAll()) {

        }

    }
}
