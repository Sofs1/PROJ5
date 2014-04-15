/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.util;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * @author Ana Sofia Mendes
 * @author Orlando Neves
 */
@FacesValidator("scaleRangeValidator")
public class ScaleRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Let required="true" handle.
        }

        UIInput minValue = (UIInput) component.getAttributes().get("minValue");

        int valMin = (int) minValue.getValue();

        int valMax = (int) value;

        if (valMax < valMin) {
            minValue.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Max value bigger than Min value. Check again.", null));
        }
    }
}
