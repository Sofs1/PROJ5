/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

/**
 *
 * @author sofia
 */
@Entity
public class Administrator extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ManyToMany
    private List<Paj> pajs;

    public Administrator() {
    }

    public List<Paj> getPajs() {
        return pajs;
    }

    public void setPajs(List<Paj> pajs) {
        this.pajs = pajs;
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
        if (!(object instanceof Administrator)) {
            return false;
        }
        Administrator other = (Administrator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "pt.uc.dei.ar.proj5.grupob.entities.Administrator[ id=" + id + " ]";
    }

}
