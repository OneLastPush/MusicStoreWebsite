package com.trouble.validators;

import com.trouble.JPAController.UsersJpaController;
import com.trouble.entities.Users;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

/**
 * A Validator class that checks if an email is unique.
 * 
 * @author Seaim Khan
 */
@Named
public class EmailValidator implements Validator
{

    @Inject
    private UsersJpaController ujc;

    /**
     * Validates uniqueness of email.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if the email already exists.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        String email = (String) value;
        Users invalid = null;
        try
        {
            invalid = ujc.findByEmail(email);
        }
        catch (NoResultException nre)
        {
        }
        if (invalid != null)
        {
            System.out.print("NOTUNIQUE: Email already exists!");
            throw new ValidatorException(new FacesMessage("This email already exists!"));
        }
    }

}
