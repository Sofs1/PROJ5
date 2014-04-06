/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.component.UIForm;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.ejbs.UserEJB;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.facades.StudentFacade;

/**
 *
 * @author sofia
 */
@Named
@ViewScoped
public class ListStudentsController {

    private List<Student> listStudentSelected;
    private List<Student> listStudentPaj;
    private List<Student> listStudentPajToProject;

    private String searchStudent;
    @Inject
    private UserEJB userLogado;
    @Inject
    private StudentFacade studentFacade;
    private UIForm table_listStudents;
//    private UIForm buttom_add;
    private UIForm table_listStudentsToProject;

    /**
     * Creates a new instance of ListStudentsController
     */
    public ListStudentsController() {

    }

    @PostConstruct
    public void initUser() {
        this.listStudentPajToProject = new ArrayList<>();
        this.listStudentSelected = new ArrayList<>();
    }
//
//    public UIForm getButtom_add() {
//        return buttom_add;
//    }
//
//    public void setButtom_add(UIForm buttom_add) {
//        this.buttom_add = buttom_add;
//    }

    public List<Student> getListStudentSelected() {
        return listStudentSelected;
    }

    public void setListStudentSelected(List<Student> listStudentSelected) {
        this.listStudentSelected = listStudentSelected;
    }

    public List<Student> getListStudentPajToProject() {
        return listStudentPajToProject;
    }

    public void setListStudentPajToProject(List<Student> listStudentPajToProject) {
        this.listStudentPajToProject = listStudentPajToProject;
    }

    public String getSearchStudent() {
        return searchStudent;
    }

    public void setSearchStudent(String searchStudent) {
        this.searchStudent = searchStudent;
    }

    public UserEJB getUserLogado() {
        return userLogado;
    }

    public void setUserLogado(UserEJB userLogado) {
        this.userLogado = userLogado;
    }

    public StudentFacade getStudentFacade() {
        return studentFacade;
    }

    public void setStudentFacade(StudentFacade studentFacade) {
        this.studentFacade = studentFacade;
    }

    public List<Student> getListStudentPaj() {
        return listStudentPaj;
    }

    public void setListStudentPaj(List<Student> listStudentPaj) {
        this.listStudentPaj = listStudentPaj;
    }

    public UIForm getTable_listStudents() {
        return table_listStudents;
    }

    public void setTable_listStudents(UIForm table_listStudents) {
        this.table_listStudents = table_listStudents;
    }

    public UIForm getTable_listStudentsToProject() {
        return table_listStudentsToProject;
    }

    public void setTable_listStudentsToProject(UIForm table_listStudentsToProject) {
        this.table_listStudentsToProject = table_listStudentsToProject;
    }

    public void showListStudents() {
        listStudentPaj = studentFacade.listStudentsPaj(userLogado.getPajSelected());
        table_listStudents.setRendered(true);
        // buttom_add.setRendered(true);
    }

    public void showSearchStudents() {
        listStudentPaj = studentFacade.getSearchByName(searchStudent, userLogado.getPajSelected());
        table_listStudents.setRendered(true);
    }

    public void addStudentsToList() {
        listStudentPajToProject.addAll(listStudentSelected);
        table_listStudentsToProject.setRendered(true);
    }

    public void removeStudentsFromList(Student st) {
        listStudentPajToProject.remove(st);
    }

}
