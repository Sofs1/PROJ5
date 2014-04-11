/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIPanel;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Criteria;
import pt.uc.dei.ar.proj5.grupob.entities.Evaluation;
import pt.uc.dei.ar.proj5.grupob.entities.Project;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.EvaluationFacade;
import pt.uc.dei.ar.proj5.grupob.facades.ProjectFacade;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;

/**
 *
 * @author Zueb LDA
 */
@Named
@RequestScoped
public class StudentReportController {

    @Inject
    private EvaluationFacade evaluationFacade;
    @Inject
    private SessionController session;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private ProjectFacade projectFacade;

    private Project selectedProject;

    private CartesianChartModel categoryModel;
    private CartesianChartModel categoryModelEvolution;
    private CartesianChartModel categoryModelEvolutionCriteria;

    private UIPanel tableEvalProj;

    public StudentReportController() {
    }

    @PostConstruct
    public void init() {
    }

    public UIPanel getTableEvalProj() {
        return tableEvalProj;
    }

    public void setTableEvalProj(UIPanel tableEvalProj) {
        this.tableEvalProj = tableEvalProj;
    }

    public EvaluationFacade getEvaluationFacade() {
        return evaluationFacade;
    }

    public void setEvaluationFacade(EvaluationFacade evaluationFacade) {
        this.evaluationFacade = evaluationFacade;
    }

    public SessionController getSession() {
        return session;
    }

    public void setSession(SessionController session) {
        this.session = session;
    }

    public CartesianChartModel getCategoryModel() {
        return categoryModel;
    }

    public void setCategoryModel(CartesianChartModel categoryModel) {
        this.categoryModel = categoryModel;
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

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public CartesianChartModel getCategoryModelEvolution() {
        return categoryModelEvolution;
    }

    public void setCategoryModelEvolution(CartesianChartModel categoryModelEvolution) {
        this.categoryModelEvolution = categoryModelEvolution;
    }

    public CartesianChartModel getCategoryModelEvolutionCriteria() {
        return categoryModelEvolutionCriteria;
    }

    public void setCategoryModelEvolutionCriteria(CartesianChartModel categoryModelEvolutionCriteria) {
        this.categoryModelEvolutionCriteria = categoryModelEvolutionCriteria;
    }

    /**
     * Generate chart result to Average response of each user in each project.
     */
    public void createCategoryModel() {
        categoryModel = new CartesianChartModel();

        for (Project p : ((Student) session.getUser()).getProjects()) {
            ChartSeries a = new ChartSeries();
            List<Evaluation> list = evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), p);
            a.setLabel(p.getName());

            for (Evaluation e : list) {
                a.set(e.getCriteria().getDescription(), e.getNote());

            }
            categoryModel.addSeries(a);

        }
    }

    public void createCategoryModelEvolution() {
        categoryModelEvolution = new CartesianChartModel();

        ChartSeries a = new ChartSeries();
        a.setLabel("Evolution");
        for (Project p : ((Student) session.getUser()).getProjects()) {
            a.set(p.getName(), evaluationFacade.avgStudentProject((Student) session.getUser(), p));
        }
        categoryModelEvolution.addSeries(a);
    }

    public void createCategoryModelEvolutionCriteria() {
        categoryModelEvolutionCriteria = new CartesianChartModel();
        ChartSeries a = new ChartSeries();

        for (Project p : ((Student) session.getUser()).getProjects()) {
            List<Evaluation> list = evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), p);

            for (Criteria c : session.getPajSelected().getCriteria()) {
                for (Evaluation e : list) {
                    a.setLabel(c.getDescription());
                    a.set(e.getProject().getName(), e.getNote());
                }
                categoryModelEvolutionCriteria.addSeries(a);
            }

        }
    }

//        for (Criteria c : session.getPajSelected().getCriteria()) {
//            ChartSeries a = new ChartSeries();
//            a.setLabel(c.getDescription());
//            
//            
//            List<Evaluation> list = evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), p);
//            
//            
//
//            for (Evaluation e : list) {
//                a.set(e.getProject().getName(), e.getNote());
//
//            }
//            categoryModelEvolutionCriteria.addSeries(a);
//        }
//        
    //  public void createCategoryModelEvolutionCriteria() {
    //        categoryModelEvolutionCriteria = new CartesianChartModel();
    //
    //        for (Project p : session.getPajSelected().getProjects()) {
    //            ChartSeries a = new ChartSeries();
    //            List<Evaluation> list = evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), p);
    //            for (Criteria c : session.getPajSelected().getCriteria()) {
    //
    //                //a.setLabel(c.getDescription());
    //                for (Evaluation e : list) {
    //                    a.setLabel(e.getCriteria().getDescription());
    //                    a.set(e.getProject().getName(), e.getNote());
    //
    //                }
    //
    //            }
    //            categoryModelEvolutionCriteria.addSeries(a);
    //        }
    //
    //    }
    /**
     * Looks to the List of Evaluations to a selected Project
     *
     * @return List of Evaluations
     */
    public List<Evaluation> studentEvaluation() {
        return evaluationFacade.evaluationsStudentToProject((Student) session.getUser(), selectedProject);
    }

    public List<Project> studentProjects() {
        return ((Student) session.getUser()).getProjects();
    }

    public void saveProjectSelected(Project selected) {
        selectedProject = selected;
        createCategoryModel();
        createCategoryModelEvolution();
        createCategoryModelEvolutionCriteria();
        tableEvalProj.setRendered(true);

    }

    public Double avgProject() {
        return evaluationFacade.avgProject(selectedProject);
    }

    public Double avgStdProject() {
        return evaluationFacade.avgStudentProject((Student) session.getUser(), selectedProject);
    }

    public Double avgEdition() {
        return evaluationFacade.avgPaj(session.getPajSelected());
    }

    public String submitEvaluationEmpty() {
        evaluationFacade.submitEmptyEvaluation((Student) session.getUser());
        return "reportsStudent";
    }
}
