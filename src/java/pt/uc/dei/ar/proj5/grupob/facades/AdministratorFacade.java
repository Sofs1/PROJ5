/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.entities.Administrator;
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.util.Encrypt;
import pt.uc.dei.ar.proj5.grupob.util.NotRegistedEmailException;
import pt.uc.dei.ar.proj5.grupob.util.PasswordException;

/**
 *
 * @author sofia
 */
@Stateless
public class AdministratorFacade extends AbstractFacade<Administrator> {

    @PersistenceContext(unitName = "PajSelfEvaluationPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorFacade() {
        super(Administrator.class);
    }

    private User getAdminbyEmail(String email) {
        Query q = em.createNamedQuery("Administrator.findByEmail");
        q.setParameter("email", email);
        try {
            User user = (Administrator) q.getSingleResult();
            return user;
        } catch (Exception e) {
            Logger.getLogger(AdministratorFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
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
    public User searchAdmin(String email, String password) throws NotRegistedEmailException, PasswordException {
        User adminTemp = getAdminbyEmail(email);
        String passEncripted = Encrypt.cryptWithMD5(password);
        Query q = em.createNamedQuery("Administrator.findAll");
        List<User> temp = q.getResultList();
        if (adminTemp == null) {

            if (temp.isEmpty()) {
                User u = new Administrator();
                u.setId(1);
                u.setName("admin");
                u.setEmail("admin@mail.com");
                u.setPass(Encrypt.cryptWithMD5("admin"));
                u.setRegistrationYear(new Date());
                Administrator a = (Administrator) u;
                this.create(a);
                return u;

            } else {
                throw new NotRegistedEmailException();
            }
        } else if (!adminTemp.getPass()
                .equals(passEncripted)) {
            throw new PasswordException();
        } else {
            return adminTemp;
        }
    }

}
