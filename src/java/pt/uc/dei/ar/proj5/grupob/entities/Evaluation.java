/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author sofia
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Evaluation.findStudent", query = "SELECT u FROM Evaluation u WHERE u.student.id = :id"),
    @NamedQuery(name = "Evaluation.findStudentProject", query = "SELECT u FROM Evaluation u WHERE u.student.id = :id_st and u.project.id = :id_proj"),
    @NamedQuery(name = "Evaluation.avgProj", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.id = :id_proj"),
    @NamedQuery(name = "Evaluation.avgPaj", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.paj.id = :id_paj"),
    @NamedQuery(name = "Evaluation.avgProjCrit", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.id = :id_proj and u.criteria.id = :id_crit"),
    @NamedQuery(name = "Evaluation.avgStudProjCrit", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.id = :id_proj and u.criteria.id = :id_crit"),
    @NamedQuery(name = "Evaluation.avgStudCrit", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.student.id = :id_st and u.criteria.id = :id_crit"),
    @NamedQuery(name = "Evaluation.avgStudProj", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.student.id = :id_st and u.project.id = :id_proj"),
    @NamedQuery(name = "Evaluation.avgStud", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.student.id = :id_st"),
    @NamedQuery(name = "Evaluation.avgAdminAnsStudProj", query = "SELECT AVG(u.note), u.student.name, u.project.name FROM Evaluation u WHERE u.project.paj = :paj group by u.student.name, u.project.name"),
    @NamedQuery(name = "Evaluation.avgAdminAnsAllStudByPaj", query = "SELECT AVG(u.note), u.student FROM Evaluation u WHERE u.project.paj = :paj group by u.student.name"),
    @NamedQuery(name = "Evaluation.avgAdminAllStudsEachAnsProj", query = "SELECT AVG(u.note), u.criteria.description, u.project.name, u.project.paj.name FROM Evaluation u WHERE u.project = :proj group by u.criteria.description, u.project.name, u.project.paj.name"),
    @NamedQuery(name = "Evaluation.avgAdminAllStudsEachCrit", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.criteria.id = :id_crit"),
    @NamedQuery(name = "Evaluation.avgAdminEachPajEdi", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.paj.id = :id_paj"), // @NamedQuery(name = "Evaluation.avgAdminAllAnsByProj", query = "SELECT AVG(u.note) FROM Evaluation u WHERE u.project.id = :id_proj"
    @NamedQuery(name = "Evaluation.avgAdminStdEachProj", query = "SELECT AVG(u.note), u.project.name FROM Evaluation u WHERE u.student = :student group by u.project.name")
})
public class Evaluation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Project project;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Criteria criteria;

    @NotNull
    private Double note;

    @Transient
    private Double average;

    public Evaluation() {
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Double getNote() {
        return note;
    }

    public void setNote(Double note) {
        this.note = note;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evaluation)) {
            return false;
        }
        Evaluation other = (Evaluation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.uc.dei.ar.proj5.grupob.entities.Evaluation[ id=" + id + " ]";
    }

}
//
//
//@NamedQuery(name = "AutoUser.usersThatDontEvaluate", query = 
//        "SELECT u.userName FROM AutoUser u WHERE u.userId NOT IN "
//                + "(SELECT e.owner.userId FROM AutoEvaluation e WHERE e.project.projectId=:projectId)"),
