/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.util;

/**
 *
 * @author Sofia Vitor
 */
public class PajDeleteException extends Exception {

    public PajDeleteException() {
        super("Cannot remove Paj edition while there are evaluations");
    }

}
