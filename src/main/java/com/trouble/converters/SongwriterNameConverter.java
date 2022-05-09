package com.trouble.converters;

import com.trouble.backing.SongwriterBackingBean;
import com.trouble.entities.Songwriters;
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
 * A Converter class for a Songwriters entity.
 * 
 * @author Seaim Khan
 */
@Named
public class SongwriterNameConverter implements Converter
{

    @Inject
    private SongwriterBackingBean songwritersController;
    
    private List<Songwriters> songwriters;
    
    /**
     * Init method that loads a list of all Songwriters entities.
     */
    @PostConstruct
    public void init(){
        this.songwriters = songwritersController.getSongwriters();    
    }
    
    /**
     * Overridden getAsObject method for a Songwriters entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Songwriters entity object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals(""))
        {
            return null;
        }
        else
        {
            try
            {
                String name = value;
                for (Songwriters s : songwriters)
                {
                    if (s.getSongwriter().equals(name))
                    {
                        return s;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid songwriter!"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for a Songwriters entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The songwriter name.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value instanceof Songwriters)
        {
            return ((Songwriters) value).getSongwriter();
        }
        else
        {
            return "";
        }
    }

}
