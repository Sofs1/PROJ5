/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.util;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
public class ExistEvaluationOnProjectException extends Exception {

    public ExistEvaluationOnProjectException() {

        super("You can´t delete project because exists evaluations.");
    }

}
