/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.ejbs;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Administrator;
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
@Stateful
@SessionScoped
public class UserEJB {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;
    
    private User user;

    protected EntityManager getEntityManager() {
        return em;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    private User getAdminbyEmail(String email) {
        Query q = em.createNamedQuery("Administrator.findByEmail");
        q.setParameter("email", email);
        try {
            User user = (Administrator) q.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User getUserbyEmail(String email) {
        if (getAdminbyEmail(email) != null) {
            return getAdminbyEmail(email);
        } else if (getStudentbyEmail(email) != null) {
            return getStudentbyEmail(email);
        } else {
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
            String passEncripted = Encrypt.encryptWithMD5(user.getPass());
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
    public User searchLogged(String email, String password) throws NotRegistedEmailException, PasswordException {
        User usertemp = getUserbyEmail(email);
        String passEncripted = Encrypt.encryptWithMD5(usertemp.getPass());
        if (usertemp == null) {
            throw new NotRegistedEmailException();
        } else if (!usertemp.getPass().equals(passEncripted)) {
            throw new PasswordException();
        } else {
            return usertemp;
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
    public void editUserFacade(User user, String passConf, String email) throws PasswordException, DuplicateEmailException {
        User usertemp = getUserbyEmail(email);
        if (!user.getPass().equals(passConf)) {
            throw new PasswordException();
        } else {
            if (usertemp == null || usertemp.getId() == user.getId()) {
                String passEncripted = Encrypt.encryptWithMD5(user.getPass());
                user.setPass(passEncripted);
                getEntityManager().merge(user);
            } else {
                throw new DuplicateEmailException();
            }
        }
    }
}
