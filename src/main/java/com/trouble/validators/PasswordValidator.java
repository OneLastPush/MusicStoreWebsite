package com.trouble.validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author Max
 */
@FacesValidator("com.trouble.PasswordValidator")
public class PasswordValidator implements Validator
{

    public PasswordValidator()
    {
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        String password = (String) value;
        String confirm = (String) component.getAttributes().get("confPassword");
        if (password == null || confirm == null)
        {
            return; // Just ignore and let required="true" do its job.
        }

        if (!password.equals(confirm))
        {
            System.out.print("PASSMATCH: passwords don't match");
            throw new ValidatorException(new FacesMessage("Passwords are not equal."));

        }
    }

}
