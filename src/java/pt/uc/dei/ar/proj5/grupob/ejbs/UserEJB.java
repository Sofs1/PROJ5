/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.ejbs;

import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import pt.uc.dei.ar.proj5.grupob.entities.User;

/**
 *
 * @author sofia
 */
@Stateful
@SessionScoped
public class UserEJB implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
