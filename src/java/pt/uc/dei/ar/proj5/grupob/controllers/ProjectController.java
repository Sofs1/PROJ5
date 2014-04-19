/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;
import pt.uc.dei.ar.proj5.grupob.util.ExistEvaluationOnProjectException;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
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
    private Project projectSelected;
    private Project flashProject;

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

    public Project getFlashProject() {
        return flashProject;
    }

    /**
     * put a selected project into a flash memory
     *
     * @param flashProject
     */
    public void setFlashProject(Project flashProject) {
        this.flashProject = flashProject;
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("project", flashProject);
    }

    /**
     * Create a new project invoking addProject(Project, Paj) from Project bean
     */
    public void createNewProject() {
        projectFacade.addProject(project, session.getPajSelected());
    }

    /**
     * remove the selected Project
     *
     * @param p Project
     */
    public String removeProject(Project p) {
        try {
            projectFacade.removeProject(p, session.getPajSelected());
        } catch (ExistEvaluationOnProjectException ex) {
            java.util.logging.Logger.getLogger(ProjectController.class.getName()).log(Level.SEVERE, null, ex);
            errorMessage(ex.getMessage());
        }
        return "adminProjects?faces-redirect=true";
    }

    /**
     * Saves selected project in flash scope to use in selected project view
     *
     * @param p
     * @return xhtml page - openProjectAdmin
     */
    public String openProject(Project p) {

        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.put("project", p);
        return "openProjectAdmin";

    }

    /**
     * add a new error message
     *
     * @param summary message
     */
    public void errorMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

}
