/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;

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
    private String erro;

    @PostConstruct
    public void init() {
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        setSelectedProject((Project) flash.get("project"));
        selectedProject = new Project();
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
        } catch (RuntimeException e) {
            Logger.getLogger(UserController.class.getName()).log(Level.SEVERE, null, e);
            erro = e.getMessage();
        }

    }

}
