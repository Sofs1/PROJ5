/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author sofia
 */
@Entity
public class Paj implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    @Column(length = 150, nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private int yearEd;

    @NotNull
    @Column(nullable = false)
    private Integer scaleMin;

    @NotNull
    @Column(nullable = false)
    private Integer scaleMax;

    @OneToMany(mappedBy = "paj", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<Student> students;

    @OneToMany(mappedBy = "paj", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Project> projects;

    @OneToMany(mappedBy = "paj", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Criteria> criteria;

    public Paj() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearEd() {
        return yearEd;
    }

    public void setYearEd(int yearEd) {
        this.yearEd = yearEd;
    }

    public Integer getScaleMin() {
        return scaleMin;
    }

    public void setScaleMin(Integer scaleMin) {
        this.scaleMin = scaleMin;
    }

    public Integer getScaleMax() {
        return scaleMax;
    }

    public void setScaleMax(Integer scaleMax) {
        this.scaleMax = scaleMax;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Criteria> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<Criteria> criteria) {
        this.criteria = criteria;
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
        if (!(object instanceof Paj)) {
            return false;
        }
        Paj other = (Paj) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.uc.dei.ar.proj5.grupob.entities.Paj[ id=" + id + " ]";
    }

}
