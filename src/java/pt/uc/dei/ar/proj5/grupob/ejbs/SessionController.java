/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.ejbs;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import pt.uc.dei.ar.proj5.grupob.entities.Paj;
import pt.uc.dei.ar.proj5.grupob.entities.Student;
import pt.uc.dei.ar.proj5.grupob.entities.User;

/**
 *
 * @author sofia
 */
@Named
@Stateful
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;
    private Paj pajSelected;

    public SessionController() {
    }

    @PostConstruct
    public void init() {

        user = null;
        pajSelected = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Paj getPajSelected() {
        return pajSelected;
    }

    public void setPajSelected(Paj pajSelected) {
        this.pajSelected = pajSelected;
    }

    public Student castStudent() {
        if (user instanceof Student) {
            return (Student) user;
        }
        return null;
    }

}
