package com.trouble.validators;

import com.trouble.JPAController.RssJpaController;
import com.trouble.entities.Rss;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A Validator class for creation of an RSS Feed in database.
 * 
 * @author Seaim Khan
 */
@Named
public class FeedValidator implements Validator
{

    @Inject
    private RssJpaController rjc;

    /**
     * Validates uniqueness of RSS Feed.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if the RSS feed already exists.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        try{
            String feed = (String) value;
            Rss invalid = rjc.findByFeed(feed);
            if (invalid != null)
            {
                System.out.print("NOTUNIQUE: Feed already exists!");
                throw new ValidatorException(new FacesMessage("This feed already exists!"));
            }
        }
        catch(Exception e){
            return;
        }
    }

}
