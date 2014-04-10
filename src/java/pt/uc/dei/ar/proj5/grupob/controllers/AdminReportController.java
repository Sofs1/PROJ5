/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import pt.uc.dei.ar.proj5.grupob.ejbs.SessionController;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.EvaluationFacade;
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
    private CartesianChartModel categoryModel;

    public AdminReportController() {
    }

    @PostConstruct
    public void init() {
        createCategoryModel();
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
    public List<Object[]> avgAdminAllStudsEachAnsProj(Project p) {

        return evaluationFacade.avgAdminAllStudsEachAnsProj(p);

    }
}
