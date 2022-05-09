package com.trouble.validators;

import java.math.BigDecimal;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 * A Validator class checking if a sale price is less than the list price.
 * 
 * @author Seaim Khan
 */
@FacesValidator("com.trouble.SalePriceValidator")
public class SalePriceValidator implements Validator{
    
    public SalePriceValidator(){}
    
    /**
     * Validates that a sale price is less than a list price.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if there is no list price or if sale price is
     *                            greater than list price.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        BigDecimal salePrice = (BigDecimal) value;
        ExternalContext ec = context.getExternalContext();
        BigDecimal listPrice;
        if (!ec.getRequestParameterMap().get("form:listPrice_input").isEmpty())
        {
            listPrice = new BigDecimal(ec.getRequestParameterMap().get("form:listPrice_input"));
        }
        else
        {
            System.out.print("SALEPRICEERROR: List price cannot be null or empty!");
            throw new ValidatorException(new FacesMessage("List price must contain a value."));
        }

        if (salePrice == null)
        {
            salePrice = new BigDecimal("0.00");
        }

        if (salePrice.compareTo(listPrice) == 0 || salePrice.compareTo(listPrice) > 0)
        {
            System.out.print("SALEPRICEERROR: Sale price is greater than or equal to list price");
            throw new ValidatorException(new FacesMessage("Sale price is invalid."));
        }
    }

}
