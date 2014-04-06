/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.ejbs.UserEJB;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.PajFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;
import pt.uc.dei.ar.proj5.grupob.util.DuplicateEmailException;
import pt.uc.dei.ar.proj5.grupob.util.PasswordException;

/**
 *
 * @author Zueb LDA
 */
@Named("registrationController")
@ConversationScoped
public class RegistrationController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Conversation conversation;
    @Inject
    private PajFacade pajFacade;
    @Inject
    private StudentFacade studentFacade;
    private Student student;
    private String passConf;
    private String erro;
    private Paj selectedPaj;
    @Inject
    private UserEJB userLogado;

    public RegistrationController() {
    }

    @PostConstruct
    public void initUser() {
        this.student = new Student();
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

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPassConf() {
        return passConf;
    }

    public void setPassConf(String passConf) {
        this.passConf = passConf;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public Paj getSelectedPaj() {
        return selectedPaj;
    }

    public void setSelectedPaj(Paj selectedPaj) {
        this.selectedPaj = selectedPaj;
    }

    public List<Paj> listAllPajs() {
        return pajFacade.findAll();
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public UserEJB getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(UserEJB userLogado) {
        this.userLogado = userLogado;
    }

    public String signUp() {
        conversation.begin();
        return "signup";

    }

    public String createStudent() {
        try {
            studentFacade.createStudent(student, passConf, selectedPaj);
            userLogado.setUser(student);
            conversation.end();
            return "templateStudent";

        } catch (PasswordException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "signup";
        } catch (DuplicateEmailException ex) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            return "signup";
        }
    }

    public String cancelView() {
        conversation.end();
        return "index";
    }

}
