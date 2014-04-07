/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;
import pt.uc.dei.ar.proj5.grupob.util.ExistEvaluationOnProjectException;

/**
 *
 * @author Zueb LDA
 */
@Named
@RequestScoped
public class ProjectController {

    @Inject
    private ProjectFacade projectFacade;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private SessionController session;
    private Project project;
    private Date begDate;
    private Date endDate;
    private Project projectSelected;
    private String erro;

    public ProjectController() {
    }

    @PostConstruct
    public void init() {
        project = new Project();
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

    public ProjectFacade getProjectFacade() {
        return projectFacade;
    }

    public void setProjectFacade(ProjectFacade projectFacade) {
        this.projectFacade = projectFacade;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getBegDate() {
        return begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SessionController getSession() {
        return session;
    }

    public void setSession(SessionController session) {
        this.session = session;
    }

    public Project getProjectSelected() {
        return projectSelected;
    }

    public void setProjectSelected(Project projectSelected) {
        this.projectSelected = projectSelected;
    }

    /**
     * Create a new project invoking addProject(Project, Paj) from Project bean
     */
    public void createNewProject() {

        project.setBegDate(begDate);
        project.setEndDate(endDate);

        projectFacade.addProject(project, session.getPajSelected());
        //return "adminProjects";
    }

    public void removeProject(Project p) {
        try {
            projectFacade.removeProject(p, session.getPajSelected());
            //return "adminProjects";
        } catch (ExistEvaluationOnProjectException ex) {
            java.util.logging.Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            erro = ex.getMessage();
            //return null;
        }
    }

    /**
     * Saves selected playlist in flash scope to use in selected playlist
     * edition
     *
     * @return String
     */
    public String openProject(Project p) {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("project", p);

        return "openProjectAdmin";

    }

    public void giveEvaluation(Project p) {
    }

    public List<Project> listOpenProjects() {
        return studentFacade.openProjects((Student) session.getUser());
    }

}
