/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.io.Serializable;
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
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.facades.EvaluationFacade;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;

/**
 *
 * @author Zueb LDA
 */
@Named("viewProjectController")
@ViewScoped
public class ViewProjectController implements Serializable {

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
    private Double avg;
    private List<Student> studentsList;
    private Student studentTemp;
    private Student temp;
    private List<Student> filteredStudents;

    private UIPanel evaluationPanel;

    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        setSelectedProject((Project) flash.get("project"));
        confirmedEvaluation = "Evaluation submitted";
    }

    public Student getTemp() {
        return temp;
    }

    public void setTemp(Student temp) {
        this.temp = temp;
    }

    public Student getStudentTemp() {
        return studentTemp;
    }

    public void setStudentTemp(Student studentTemp) {
        this.studentTemp = studentTemp;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(Double avg) {
        this.avg = avg;
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

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<Student> studentsList) {
        this.studentsList = studentsList;
    }

    public List<Student> getFilteredStudents() {
        return filteredStudents;
    }

    public void setFilteredStudents(List<Student> filteredStudents) {
        this.filteredStudents = filteredStudents;
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
        evaluationFacade.submitEvaluations(studentEvaluations);
        openEvaluation(projectSelected);
    }

    public List<Project> listOpenProjects() {
        return studentFacade.openProjects((Student) session.getUser());
    }

    public List<Project> listClosedProjects() {
        return studentFacade.closedProjects((Student) session.getUser());
    }

    public void openEvaluation(Project p) {
        projectSelected = p;
        this.studentEvaluations = evaluationFacade.studentEvaluationsSetCriteria((Student) session.getUser(), p);
        evaluationFacade.averageCriteriaProj(studentEvaluations);
        avg = evaluationFacade.avgProject(p);
        evaluationPanel.setRendered(true);
    }

    public List<Student> returnListStudents() {
        return studentFacade.listStudentsPaj(loggedUser.getPajSelected(), selectedProject);
    }

    public List<Evaluation> givenEvaluations() {
        Student temp = (Student) session.getUser();
        return evaluationFacade.evaluationsStudentToProject(temp, projectSelected);
    }

    public void addStudentsToProject() {

        List<Student> list = studentsList;
        Project p = selectedProject;
        projectFacade.addUsersToProject(studentsList, p);

    }

    public void viewStudentEvaluation(User s) {
        studentTemp = (Student) s;
        studentEvaluations = evaluationFacade.evaluationsStudentToProject(studentTemp, projectSelected);
        evaluationPanel.setRendered(true);
        temp = (Student) s;
        avg = evaluationFacade.avgStudentProject(studentTemp, projectSelected);
    }

    public List<Evaluation> listEvaluationStudent(User s) {
        studentTemp = (Student) s;
        return evaluationFacade.evaluationsStudentToProject(studentTemp, selectedProject);
    }

    public String verifySubmitedEvaluation() {
        if (evaluationFacade.verifyEvaluation(studentEvaluations)) {
            return "confirmEval";
        } else {
            return "confirmAny";
        }
    }

}
