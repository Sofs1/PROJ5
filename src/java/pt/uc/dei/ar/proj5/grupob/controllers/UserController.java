/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Log;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.facades.AdministratorFacade;
import pt.uc.dei.ar.proj5.grupob.facades.LogFacade;
import pt.uc.dei.ar.proj5.grupob.facades.PajFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;
import pt.uc.dei.ar.proj5.grupob.util.DuplicateEmailException;
import pt.uc.dei.ar.proj5.grupob.util.NotRegistedEmailException;
import pt.uc.dei.ar.proj5.grupob.util.PasswordException;

/**
 *
 * @author sofia
 */
@Named
@ViewScoped
public class UserController {

    @Inject
    private PajFacade pajFacade;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private AdministratorFacade adminFacade;
    @Inject
    private SessionController userEJB;
    private String passConf;
    private Student student;
    private User studentEdit;
    private User user;
    private String erro;
    private Paj selectedPaj;
    @Inject
    private LogFacade logFacade;
    private Log log;
    private UIForm choosePaj;
    private Student stWithoutPaj;

    /**
     * Creates a new instance of userController
     */
    public UserController() {
    }

    @PostConstruct
    public void initUser() {
        this.student = new Student();
        this.user = new User();
        this.selectedPaj = new Paj();
        this.log = new Log();
    }

    public Student getStWithoutPaj() {
        return stWithoutPaj;
    }

    public void setStWithoutPaj(Student stWithoutPaj) {
        this.stWithoutPaj = stWithoutPaj;
    }

    public UIForm getChoosePaj() {
        return choosePaj;
    }

    public void setChoosePaj(UIForm choosePaj) {
        this.choosePaj = choosePaj;
    }

    public LogFacade getLogFacade() {
        return logFacade;
    }

    public void setLogFacade(LogFacade logFacade) {
        this.logFacade = logFacade;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public Paj getSelectedPaj() {
        return selectedPaj;
    }

    public void setSelectedPaj(Paj selectedPaj) {
        this.selectedPaj = selectedPaj;
    }

    public SessionController getUserEJB() {
        return userEJB;
    }

    public void setUserEJB(SessionController userEJB) {
        this.userEJB = userEJB;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public String getErro() {
        return erro;
    }

    public String getPassConf() {
        return passConf;
    }

    public void setPassConf(String passConf) {
        this.passConf = passConf;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PajFacade getPajFacade() {
        return pajFacade;
    }

    public void setPajFacade(PajFacade pajFacade) {
        this.pajFacade = pajFacade;
    }

    public StudentFacade getStudentFacade() {
        return studentFacade;
    }

    public void setStudentFacade(StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    public AdministratorFacade getAdminFacade() {
        return adminFacade;
    }

    public void setAdminFacade(AdministratorFacade adminFacade) {
        this.adminFacade = adminFacade;
    }

    public User getStudentEdit() {
        return (Student) userEJB.getUser();
    }

    public void setStudentEdit(User studentEdit) {
        this.studentEdit = studentEdit;
    }

    /**
     * search the student to logger
     *
     * @return xhtml navigation
     */
    public String searchLogged() {

        try {
            if (((Student) studentFacade.searchStudent(user.getEmail(), user.getPass())).getPaj() == null) {
                stWithoutPaj = (Student) studentFacade.searchStudent(user.getEmail(), user.getPass());
                choosePaj.setRendered(true);
                return null;
            }
            userEJB.setUser(studentFacade.searchStudent(user.getEmail(), user.getPass()));
            userEJB.setPajSelected(((Student) userEJB.getUser()).getPaj());
            log.setStudent((Student) userEJB.getUser());
            log.setTask("Success - searchLogged()");
            logFacade.createLog(log);
            return "openProjectsStudent";
        } catch (NotRegistedEmailException | PasswordException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            log.setTask("Failed - searchLogged()");
            logFacade.createLog(log);
            return "index";
        }
    }

    /**
     * search the administrator to logger
     *
     * @return xhtml navigation
     */
    public String searchAdmin() {
        try {
            userEJB.setUser(adminFacade.searchAdmin(user.getEmail(), user.getPass()));
            return "adminHome";
        } catch (NotRegistedEmailException | PasswordException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "adminLogin";
        }
    }

    /**
     * list all the pajs editions
     *
     * @return List of pajs Editions
     */
    public List<Paj> listAllPajs() {
        return pajFacade.findAll();
    }

    /**
     * Delete the logged user
     *
     * @return xhtml navigation
     */
    public String removeUser() {
        log.setStudent((Student) userEJB.getUser());
        log.setTask("Success - searchLogged()");
        logFacade.createLog(log);
        studentFacade.deleteStudent((Student) userEJB.getUser(), userEJB.getPajSelected());

        return "index";
    }

    /**
     * logout the administrator in session and ends session
     *
     * @return xhtml navigation
     */
    public String logoutAdm() {
        userEJB.setUser(null);
        userEJB.setPajSelected(null);
        invalidateSession();
        return "index";
    }

    /**
     * logout the student in session and ends session
     *
     * @return xhtml navigation
     */
    public String logoutStud() {
        log.setStudent((Student) userEJB.getUser());
        userEJB.setUser(null);
        userEJB.setPajSelected(null);
        invalidateSession();
        log.setStudent((Student) userEJB.getUser());
        log.setTask("Success - logoutStud()");
        logFacade.createLog(log);
        return "index";
    }

    /**
     * ends session
     */
    private void invalidateSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }

    /**
     * edit the student logged
     *
     * @return xhtml navigation
     */
    public String editStudent() {
        try {
            studentFacade.editStudentFacade(studentEdit, passConf, userEJB.getUser().getEmail());
            log.setStudent((Student) userEJB.getUser());
            log.setTask("Success - editStudent()");
            logFacade.createLog(log);
            return "openProjectsStudent";
        } catch (PasswordException | DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return null;
        }
    }

    /**
     * set the edition saved on session to null
     *
     * @return xhtml navigation
     */
    public String homeAdmin() {
        userEJB.setPajSelected(null);
        return "adminHome";
    }

    public void savePajSelected(Paj paj) {
        studentFacade.editPajStudent(stWithoutPaj, paj);
        userEJB.setPajSelected(paj);
        choosePaj.setRendered(false);
    }

    /**
     * create a New Administrator
     *
     * @return xhtml navigation
     */
    public String createAdmin() {
        try {
            studentFacade.createStudent(student, passConf, selectedPaj);
            userEJB.setUser(student);
            return "templateStudent";

        } catch (PasswordException | DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "signup";
        }
    }

}
