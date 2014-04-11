/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DonutChartModel;
import org.primefaces.model.chart.LineChartSeries;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
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
public class AdminReportController {

    @Inject
    private EvaluationFacade evaluationFacade;
    @Inject
    private SessionController session;
    @Inject
    private StudentFacade studentFacade;
    @Inject
    private ProjectFacade projectFacade;
    private Project selectedProject;
    private boolean renderedTable;
    private CartesianChartModel categoryModel;
    private CartesianChartModel categoryModel2;
    private CartesianChartModel categoryModel3;
    private UIComponent tableGenerated;
    private DonutChartModel donutModel;
    private CartesianChartModel linearModel;
    private CartesianChartModel linearModel2;

    public AdminReportController() {
    }

    @PostConstruct
    public void init() {
        createCategoryModel();
        createChartEachStudentAllProj();
        createChartAvgAdminAllStudsEachAnsProj();
        createChartToOverallResult();
        createChartEvolThroProj();
        this.renderedTable = false;
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

    public UIComponent getTableGenerated() {
        return tableGenerated;
    }

    public void setTableGenerated(UIComponent tableGenerated) {
        this.tableGenerated = tableGenerated;
    }

    public boolean isRenderedTable() {
        return renderedTable;
    }

    public void setRenderedTable(boolean renderedTable) {
        this.renderedTable = renderedTable;
    }

    public CartesianChartModel getCategoryModel2() {
        return categoryModel2;
    }

    public void setCategoryModel2(CartesianChartModel categoryModel2) {
        this.categoryModel2 = categoryModel2;
    }

    public DonutChartModel getDonutModel() {
        return donutModel;
    }

    public void setDonutModel(DonutChartModel donutModel) {
        this.donutModel = donutModel;
    }

    public CartesianChartModel getCategoryModel3() {
        return categoryModel3;
    }

    public void setCategoryModel3(CartesianChartModel categoryModel3) {
        this.categoryModel3 = categoryModel3;
    }

    public CartesianChartModel getLinearModel() {
        return linearModel;
    }

    public void setLinearModel(CartesianChartModel linearModel) {
        this.linearModel = linearModel;
    }

    public CartesianChartModel getLinearModel2() {
        return linearModel2;
    }

    public void setLinearModel2(CartesianChartModel linearModel2) {
        this.linearModel2 = linearModel2;
    }

    /**
     * Average response of each user in each project
     *
     * @return
     */
    public List<Object[]> averageAnsEachStudEachProj() {

        return evaluationFacade.avgAdminAnsStudProj(session.getPajSelected());
    }

    /**
     * Generate chart result to Average response of each user in each project.
     */
    public void createCategoryModel() {

        List<Student> list = studentFacade.allStudents(this.session.getPajSelected().getId());

        categoryModel = new CartesianChartModel();

        for (int i = 0; i < list.size(); i++) {
            ChartSeries a = new ChartSeries();
            String est = list.get(i).getName();
            a.setLabel(est);

            List<Object[]> data = evaluationFacade.avgAdminStdEachProj(list.get(i));

            if (!data.isEmpty()) {
                a.setLabel(est);

                for (int j = 0; j < data.size(); j++) {

                    Double avg = (Double) data.get(j)[0];
                    String proj = (String) data.get(j)[1];

                    a.set(proj, avg);
                }
                categoryModel.addSeries(a);
            }
        }
    }

    /**
     * Calls Evaluation bean method in order to get list of average responses of
     * each user for all projects undertaken by edition.
     *
     * @return
     */
    public List<Object[]> avgAdminAnsAllStudByPaj() {

        return evaluationFacade.avgAdminAnsAllStudByPaj(session.getPajSelected());
    }

    /**
     * Average of all users for each response in each project
     *
     * @return
     */
    public List<Object[]> avgAdminAllStudsEachAnsProj() {

        //this.tableGenerated.setRendered(true);
        return evaluationFacade.avgAdminAllStudsEachAnsProj(this.selectedProject);

    }

    /**
     * All projects in data base
     *
     * @return List<Project>
     */
    public List<Project> allProjects() {

        return projectFacade.allProjects();
    }

    /**
     * Average of all users for each response in all projects
     *
     * @return List<Object[]>
     */
    public List<Object[]> avgAdminAllStudsEachCrit() {

        return evaluationFacade.avgAdminAllStudsEachCrit(session.getPajSelected());
    }

    /**
     * Overall results between different editions
     *
     * @return List<Object[]>
     */
    public List<Object[]> avgAdminEachPajEdi() {

        return evaluationFacade.avgAdminEachPajEdi();
    }

    /**
     * Evolution of the average responses of all students throughout the project
     *
     * @return List<Object[]>
     */
    public List<Object[]> avgAdminAllAnsByProj() {

        return evaluationFacade.avgAdminAllAnsByProj(session.getPajSelected());
    }

    /**
     * Set table rendered
     */
    public void makeRendered() {

        this.renderedTable = true;
        createChartAllProjStats();
    }

    //Charts 
    /**
     * Generate chart result to Average response of each user in each project.
     */
    public void createChartEachStudentAllProj() {

        List<Object[]> list = evaluationFacade.avgAdminAnsAllStudByPaj(session.getPajSelected());

        categoryModel2 = new CartesianChartModel();

        if (!list.isEmpty()) {

            for (int i = 0; i < list.size(); i++) {
                ChartSeries a = new ChartSeries();

                String student = (String) list.get(i)[1];
                double avg = (Double) list.get(i)[0];
                a.setLabel(student);
                a.set("Students", avg);

                categoryModel2.addSeries(a);

            }
        }
    }

    /**
     * Generate chart result to Average of all projects
     */
    public void createChartAllProjStats() {

        donutModel = new DonutChartModel();

        List<Object[]> list = evaluationFacade.avgAdminAllStudsEachAnsProj(this.selectedProject);
        Map<String, Number> circle = new LinkedHashMap<String, Number>();

        for (int i = 0; i < list.size(); i++) {

            circle.put((String) list.get(i)[1], (Double) list.get(i)[0]);
        }
        donutModel.addCircle(circle);

    }

    /**
     * Generate chart result to Average of all users for each response in all
     * projects in Paj Edition
     */
    public void createChartAvgAdminAllStudsEachAnsProj() {

        categoryModel3 = new CartesianChartModel();
        List<Object[]> list = evaluationFacade.avgAdminAllStudsEachCrit(session.getPajSelected());

        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                ChartSeries a = new ChartSeries();
                a.setLabel((String) list.get(i)[1]);
                a.set("Criteria", (Double) list.get(i)[0]);
                categoryModel3.addSeries(a);
            }
        }
    }

    /**
     * Generate chart result to Overall result
     */
    public void createChartToOverallResult() {

        linearModel = new CartesianChartModel();

        List<Object[]> list = evaluationFacade.avgAdminEachPajEdi();
        LineChartSeries a = new LineChartSeries();
        a.setLabel("Paj Edition");
        for (int i = 0; i < list.size(); i++) {
            a.set((String) list.get(i)[1], (Double) list.get(i)[0]);
            linearModel.addSeries(a);
        }
    }

    /**
     * Generate chart that marks the evolution throught project in each edition
     */
    public void createChartEvolThroProj() {

        linearModel2 = new CartesianChartModel();

        List<Object[]> list = evaluationFacade.avgAdminAllAnsByProj(session.getPajSelected());

        LineChartSeries a = new LineChartSeries();
        a.setLabel("Projects");
        for (int i = 0; i < list.size(); i++) {
            a.set((String) list.get(i)[1], (Double) list.get(i)[0]);
            a.setMarkerStyle("diamond");
            linearModel2.addSeries(a);
        }
    }

}
