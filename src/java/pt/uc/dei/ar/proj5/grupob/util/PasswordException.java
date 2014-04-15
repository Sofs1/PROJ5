package pt.uc.dei.ar.proj5.grupob.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
public class PasswordException extends Exception {

    public PasswordException() {
        super("Password doesn´t match");
    }

}
