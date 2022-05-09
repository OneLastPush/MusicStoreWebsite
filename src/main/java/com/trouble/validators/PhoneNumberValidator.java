package com.trouble.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * A Validator class that checks if there's a home number or cell number.
 * 
 * @author Seaim Khan
 */
@FacesValidator("com.trouble.PhoneNumberValidator")
public class PhoneNumberValidator implements Validator
{

    public PhoneNumberValidator()
    {
    }

    /**
     * Validates if there is at least one phone number entered.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if both phone numbers are missing.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        String home = (String) value;
        ExternalContext ec = context.getExternalContext();
        String cell = ec.getRequestParameterMap().get("form:userCellNum");

        if ((home == null || home.isEmpty()) && (cell == null || cell.isEmpty()))
        {
            System.out.print("NONUMBER: Missing phone numbers");
            throw new ValidatorException(new FacesMessage("There must be at least one phone number!"));
        }
    }
}
