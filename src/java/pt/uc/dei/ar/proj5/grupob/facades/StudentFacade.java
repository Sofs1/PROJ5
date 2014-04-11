/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.facades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pt.uc.dei.ar.proj5.grupob.controllers.UserController;
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
            Logger.getLogger(StudentFacade.class.getName()).log(Level.SEVERE, null, e);
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
            user.setRegistrationYear(new Date());
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

    public void editPajStudent(Student st, Paj paj) {
        st.setPaj(paj);
        edit(st);
        paj.getStudents().add(st);
        em.merge(st);
    }

    /**
     * first remove all the Student traces and then remove the Student
     *
     * @param student
     * @param paj
     */
    public void deleteStudent(Student student, Paj paj) {
        remove(em.merge(student));
    }

    /**
     * Look for the Project's List from that student with evaluation in course
     *
     * @param s (student)
     * @return Project's List
     */
    public List<Project> openProjects(Student s) {
        List<Project> temp = new ArrayList<>();
        for (Project p : s.getProjects()) {
            if (!p.getBegDate().after(new Date()) && !p.getEndDate().before(new Date())) {
                temp.add(p);
            }
        }
        return temp;
    }

    /**
     * Look for the Project's List from that student with evaluation in course
     *
     * @param s (student)
     * @return Project's List
     */
    public List<Project> closedProjects(Student s) {
        List<Project> temp = new ArrayList<>();
        for (Project p : s.getProjects()) {
            if (p.getEndDate().before(new Date()) || p.getBegDate().after(new Date())) {
                temp.add(p);
            }
        }
        return temp;
    }

    public List<Student> listStudentsPaj(Paj paj, Project proj) {

        Query q = em.createNamedQuery("Student.findByPajNoProject");

        q.setParameter("paj", paj);
        q.setParameter("project", proj);
        try {
            return (List<Student>) q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(StudentFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Look from the list of Paj'Students for the Students with that string in
     * the name
     *
     * @param str
     * @param paj
     * @return Student's list
     */
    public List<Student> getSearchByName(String str, Paj paj) {
        Query q = em.createNamedQuery("Student.searchStudents");
        q.setParameter("name", "%" + str + "%").setParameter("paj", paj);
        try {
            return (List<Student>) q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(StudentFacade.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    /**
     * Method to send warning email to selected user
     *
     * @param s
     * @throws RuntimeException
     * @throws MessagingException
     */
    public void sendMail(Student s) throws RuntimeException, MessagingException {

        final String email = "acertarorumo@gmail.com";
        final String password = "managedbean";

        Properties props = System.getProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("acertarorumo@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(s.getEmail()));
            message.setSubject("Teste");
            message.setText("Dear Student,"
                    + "\n\n must submit your Evaluation");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Receives paj edition, and return list of students from NamedQuery
     *
     * @param p
     * @return
     */
    public List<Student> allStudents(Long p) {
        Query q = em.createNamedQuery("Student.findByPaj");
        q.setParameter("paj", p);
        return (List<Student>) q.getResultList();
    }

}
