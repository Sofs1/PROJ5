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
import pt.uc.dei.ar.proj5.grupob.entities.Log;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.entities.User;
import pt.uc.dei.ar.proj5.grupob.facades.EvaluationFacade;
import pt.uc.dei.ar.proj5.grupob.facades.LogFacade;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
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
    @Inject
    private LogFacade logFacade;
    private Log log;

    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        setSelectedProject((Project) flash.get("project"));
        confirmedEvaluation = "Evaluation submitted";
        this.log = new Log();
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

    /**
     *
     * @param s student
     * @return yes/no
     */
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

    /**
     * send a mail to the selected student
     *
     * @param s student
     */
    public void sendMailToStudent(Student s) {
        try {
            studentFacade.sendMail(s);
        } catch (MessagingException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            erro = e.getMessage();
        }

    }

    /**
     * submit the student Evaluation
     */
    public void giveEvaluation() {
        evaluationFacade.submitEvaluations(studentEvaluations);
        openEvaluation(projectSelected);
        log.setStudentID(session.getUser().getId());
        log.setTask("Success - giveEvaluation()");
        logFacade.createLog(log);
    }

    /**
     *
     * @return list of open Projects
     */
    public List<Project> listOpenProjects() {
        log.setStudentID(session.getUser().getId());
        log.setTask("Success - listOpenProjects()");
        logFacade.createLog(log);
        return studentFacade.openProjects((Student) session.getUser());
    }

    /**
     *
     * @return list of closed Projects
     */
    public List<Project> listClosedProjects() {
        log.setStudentID(session.getUser().getId());
        log.setTask("Success - listClosedProjects()");
        logFacade.createLog(log);
        return studentFacade.closedProjects((Student) session.getUser());
    }

    /**
     * open a Evaluation from the selected project
     *
     * @param p Project
     */
    public void openEvaluation(Project p) {
        projectSelected = p;
        this.studentEvaluations = evaluationFacade.studentEvaluationsSetCriteria((Student) session.getUser(), p);
        evaluationFacade.averageCriteriaProj(studentEvaluations);
        avg = evaluationFacade.avgProject(p);
        evaluationPanel.setRendered(true);
        log.setStudentID(session.getUser().getId());
        log.setTask("Success - openEvaluation(Project p)");
        logFacade.createLog(log);
    }

    /**
     *
     * @return Student's list
     */
    public List<Student> returnListStudents() {
        return studentFacade.listStudentsPaj(loggedUser.getPajSelected(), selectedProject);
    }

    /**
     *
     * @return List of evaluations of the logged student on the selected project
     */
    public List<Evaluation> givenEvaluations() {
        log.setStudentID(session.getUser().getId());
        log.setTask("Success - givenEvaluations()");
        logFacade.createLog(log);
        return evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), projectSelected);
    }

    /**
     * adds a Student list to th selected Project
     */
    public void addStudentsToProject() {

        List<Student> list = studentsList;
        Project p = selectedProject;
        projectFacade.addUsersToProject(studentsList, p);

    }

    /**
     * view Student Evaluation
     *
     * @param s Student
     */
    public void viewStudentEvaluation(User s) {
        studentEvaluations = evaluationFacade.evaluationsStudentToProject((Student) s, selectedProject);
        evaluationPanel.setRendered(true);
        temp = (Student) s;
        avg = evaluationFacade.avgStudentProject((Student) s, selectedProject);

    }

    /**
     *
     * @param s Student
     * @return Evaluation List from the selected User
     */
    public List<Evaluation> listEvaluationStudent(User s) {
        studentTemp = (Student) s;
        return evaluationFacade.evaluationsStudentToProject(studentTemp, selectedProject);
    }

    /**
     * verify if the submitted Evaluation was changed from the minimum values
     *
     * @return dialog_Name that will open
     */
    public String verifySubmitedEvaluation() {
        int scaleMin = loggedUser.getPajSelected().getScaleMin();
        int count = 0;
        for (Evaluation e : studentEvaluations) {
            if (e.getNote() == scaleMin) {
                count++;
            }
        }

        if (studentEvaluations.size() == count) {
            return "confirmEval";
        } else {
            return "confirmAny";
        }
    }

}
