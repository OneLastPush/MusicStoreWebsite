package com.trouble.converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Max
 */
@FacesConverter("com.convert.PostalCode")
public class PostalCodeConverter implements Converter
{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {

        value = value.toUpperCase();
        StringBuilder postal = new StringBuilder(value);
        System.out.print(postal);
        for (int i = 0; i < postal.length(); i++)
        {
            char ch = postal.charAt(i);
            if (Character.isLetter(ch) | Character.isDigit(ch))
            {
                Character.toUpperCase(postal.charAt(i));

            }
            else if (Character.isWhitespace(ch))
            {
                postal.deleteCharAt(i);
            }
            else
            {
                System.out.println("POSTAL ERROR " + postal.toString());
                return null;
            }

        }

        System.out.println("POSTAL SUCCESS " + postal.toString());
        return postal.toString();

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        String val = value.toString();
        val = val.toUpperCase();
        StringBuilder postal1 = new StringBuilder(val);
        System.out.print(postal1);
        for (int i = 0; i < postal1.length(); i++)
        {
            char ch = postal1.charAt(i);
            if (Character.isLetter(ch) | Character.isDigit(ch))
            {
                Character.toUpperCase(postal1.charAt(i));

            }
            else if (Character.isWhitespace(ch))
            {
                postal1.deleteCharAt(i);
            }
            else
            {

                System.out.println("POSTAL ERROR " + postal1.toString());
                String message = "Postal code is Wrong";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
                return null;
            }

        }

        System.out.println("POSTAL SUCCESS " + postal1.toString());
        int boundary = 3;
        StringBuilder postal = new StringBuilder();
        String code = postal1.toString();
        postal.append(code.substring(0, boundary));
        postal.append(" ");
        postal.append(code.substring(boundary));
        System.out.println("POSTAL ERROR " + postal.toString());
        return postal.toString();
    }

}
