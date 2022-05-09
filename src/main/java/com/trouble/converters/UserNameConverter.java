
package com.trouble.converters;

import com.trouble.backing.UserBackingBean;
import com.trouble.entities.Users;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Seaim Khan
 */
@Named
public class UserNameConverter implements Converter{

    @Inject
    private UserBackingBean ujc;
    
    private List<Users> users;
    
    /**
     * Init method that loads a list of all Users entities.
     */
    @PostConstruct
    public void init(){
        this.users = ujc.getUsers();
    }
    
    /**
     * Overridden getAsObject method for a Users entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Users entity object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value.trim().equals(""))
            return null;
        else{
            try{
                String name = value;
                for(Users u : users)
                    if(u.getEmail().equals(name))
                        return u;
            }
            catch(NumberFormatException e){
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid user email!"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for a Users entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The user email.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof Users)
            return ((Users)value).getEmail();
        else
            return "";
    }
    
}
