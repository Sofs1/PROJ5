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
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import pt.uc.dei.ar.proj5.grupob.ejbs.UserEJB;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.facades.AdministratorFacade;
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
@RequestScoped
public class UserController {
    
    @Inject
    private PajFacade pajFacade;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private AdministratorFacade adminFacade;
    @Inject
    private UserEJB userEJB;
    private String passConf;
    private Student student;
    private User user;
    private String erro;
    private Paj selectedPaj;

    /**
     * Creates a new instance of userController
     */
    public UserController() {
    }
    
    @PostConstruct
    public void initUser() {
        this.student = new Student();
        this.user = new User();
    }
    
    public Paj getSelectedPaj() {
        return selectedPaj;
    }
    
    public void setSelectedPaj(Paj selectedPaj) {
        this.selectedPaj = selectedPaj;
    }
    
    public UserEJB getUserEJB() {
        return userEJB;
    }
    
    public void setUserEJB(UserEJB userEJB) {
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
    
    public String createStudent() {
        
        try {
            studentFacade.createStudent(student, passConf);
            student.setPaj(selectedPaj);
            selectedPaj.getStudents().add(student);
            studentFacade.edit(student);
            pajFacade.edit(selectedPaj);
            return "index";
        } catch (PasswordException | DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "signup";
        }
    }
    
    public String searchLogged() {
        try {
            userEJB.setUser(studentFacade.searchStudent(user.getEmail(), user.getPass()));
            return "templateStudent";
        } catch (NotRegistedEmailException | PasswordException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "index";
        }
    }
    
    public String searchAdmin() {
        try {
            userEJB.setUser(adminFacade.searchAdmin(user.getEmail(), user.getPass()));
            return "adminLandingPage";
        } catch (NotRegistedEmailException | PasswordException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "adminLogin";
        }
    }
    
    public List<Paj> listAllPajs() {
        return pajFacade.findAll();
    }
    
    public String editUser() {
        try {
            studentFacade.editStudentFacade(student, passConf, userEJB.getUser().getEmail());
            return "listMusics";
        } catch (PasswordException | DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return null;
        }
        
    }
    
    public void removeUser() {
        studentFacade.deleteStudent((Student) userEJB.getUser(), userEJB.getPajSelected());
    }
    
    public String logoutAdm() {
        userEJB.setUser(null);
        userEJB.setPajSelected(null);
        invalidateSession();
        return "index";
    }
    
    public String logoutStud() {
        userEJB.setUser(null);
        invalidateSession();
        return "index";
    }
    
    private void invalidateSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }
    
    public String editStudent() {
        try {
            studentFacade.editStudentFacade(student, passConf, userEJB.getUser().getEmail());
            return "listMusics";
        } catch (PasswordException | DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return null;
        }
        
    }
    
}
