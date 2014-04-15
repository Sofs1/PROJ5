/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
@FacesValidator("actualDateValidator")
public class ActualDateValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Let required="true" handle.
        }

        //UIInput yearComponent = (UIInput) component.getAttributes().get("yearComponent");
        GregorianCalendar gc = new GregorianCalendar();
        int todayYear = gc.get(Calendar.YEAR);

        //int insertedyear = (Integer) yearComponent.getValue();
        int insertedyear = (int) value;

        if (insertedyear < todayYear) {
            //yearComponent.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "The year inserted must be bigger or equal to " + todayYear, null));
        }
    }
}
