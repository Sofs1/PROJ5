/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.uc.dei.ar.proj5.grupob.util;

import java.util.Date;
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
@FacesValidator("dateRangeValidator")
public class dateRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return; // Let required="true" handle.
        }

        UIInput startDateComponent = (UIInput) component.getAttributes().get("startDateComponent");

        Date startDate = (Date) startDateComponent.getValue();

        Date endDate = (Date) value;

        if (startDate.after(endDate)) {
            startDateComponent.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Start date may not be after end date.", null));
        } else if (endDate.before(new Date())) {

            startDateComponent.setValid(false);
            throw new ValidatorException(new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "You can´t start a project in a past date, sorry.", null));
        }
    }
}
