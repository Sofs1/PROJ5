/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.Future;
import pt.uc.dei.ar.proj5.grupob.ejbs.UserEJB;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;

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
    private UserEJB session;
    private Project project;
    @Future
    private Date begDate;
    private Date endDate;

    public ProjectController() {
    }

    @PostConstruct
    public void init() {

        project = new Project();
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

    public String createNewProject() {

        project.setBegDate(begDate);
        project.setEndDate(endDate);

        projectFacade.addProject(project, session.getPajSelected());
        return "insidePajEdition";
    }

}
