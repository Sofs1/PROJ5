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
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Evaluation;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.EvaluationFacade;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;
import pt.uc.dei.ar.proj5.grupob.util.NoEntriesToEvaluation;

/**
 *
 * @author Zueb LDA
 */
@Named("viewProjectController")
@ViewScoped
public class ViewProjectController {

    private Project selectedProject;
    @Inject
    private ProjectFacade projectFacade;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private EvaluationFacade evaluationFacade;
    @Inject
    private SessionController session;
    @Inject
    private SessionController loggedUser;
    private String erro;
    private String confirmedEvaluation;
    private Project projectSelected;
    private Evaluation evaluation;
    private List<Evaluation> studentEvaluations;

    private UIPanel evaluationPanel;

    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        setSelectedProject((Project) flash.get("project"));
    }

    public EvaluationFacade getEvaluationFacade() {
        return evaluationFacade;
    }

    public void setEvaluationFacade(EvaluationFacade evaluationFacade) {
        this.evaluationFacade = evaluationFacade;
    }

    public List<Evaluation> getStudentEvaluations() {
        return studentEvaluations;
    }

    public void setStudentEvaluations(List<Evaluation> studentEvaluations) {
        this.studentEvaluations = studentEvaluations;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public String getConfirmedEvaluation() {
        return confirmedEvaluation;
    }

    public void setConfirmedEvaluation(String confirmedEvaluation) {
        this.confirmedEvaluation = confirmedEvaluation;
    }

    public SessionController getSession() {
        return session;
    }

    public void setSession(SessionController session) {
        this.session = session;
    }

    public StudentFacade getStudentFacade() {
        return studentFacade;
    }

    public void setStudentFacade(StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }

    public Project getProjectSelected() {
        return projectSelected;
    }

    public void setProjectSelected(Project projectSelected) {
        this.projectSelected = projectSelected;
    }

    public UIPanel getEvaluationPanel() {
        return evaluationPanel;
    }

    public void setEvaluationPanel(UIPanel evaluationPanel) {
        this.evaluationPanel = evaluationPanel;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project project) {
        this.selectedProject = project;
    }

    public ProjectFacade getProjectFacade() {
        return projectFacade;
    }

    public void setProjectFacade(ProjectFacade projectFacade) {
        this.projectFacade = projectFacade;
    }

    public SessionController getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(SessionController loggedUser) {
        this.loggedUser = loggedUser;
    }

    public String submitionSearch(Student s) {

        String result = "No";
        boolean found = false;
        for (int i = 0; i < s.getEvaluations().size() && !found; i++) {

            if (s.getEvaluations().get(i).getProject().equals(selectedProject)) {

                result = "yes";
                found = true;
            }
        }
        return result;
    }

    public void sendMailToStudent(Student s) {
        try {
            studentFacade.sendMail(s);
        } catch (MessagingException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            erro = e.getMessage();
        }

    }

    public void giveEvaluation() {
        try {
            evaluationFacade.giveEvaluation(studentEvaluations);
            confirmedEvaluation = "Evaluation submitted";
        } catch (NoEntriesToEvaluation ex) {
            Logger.getLogger(ViewProjectController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
        }
    }

    public List<Project> listOpenProjects() {
        return studentFacade.openProjects((Student) session.getUser());
    }

    public void openEvaluation(Project p) {
        projectSelected = p;
        this.studentEvaluations = evaluationFacade.studentEvaluationsSetCriteria((Student) session.getUser(), p);
        evaluationPanel.setRendered(true);
    }

    public List<Student> returnListStudents() {
        return studentFacade.listStudentsPaj(loggedUser.getPajSelected(), selectedProject);
        //table_listStudents.setRendered(true);
        // buttom_add.setRendered(true);
    }

    public List<Evaluation> givenEvaluations() {
        return evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), projectSelected);
    }

}
