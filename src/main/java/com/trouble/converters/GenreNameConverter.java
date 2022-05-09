package com.trouble.converters;

import com.trouble.backing.GenreBackingBean;
import com.trouble.entities.Genres;
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
 * A Converter class for a Genres entity.
 * 
 * @author Seaim Khan
 */
@Named
public class GenreNameConverter implements Converter
{

    @Inject
    private GenreBackingBean genresController;
    
    private List<Genres> genres;
    
    /**
     * Init method that loads a list of all Genres entities.
     */
    @PostConstruct
    public void init(){
        this.genres = genresController.getGenres();    
    }
    
    /**
     * Overridden getAsObject method for a Genres entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Genres entity object.
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
                for (Genres g : genres)
                {
                    if (g.getGenreName().equals(name))
                    {
                        return g;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid genre!"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for a Genres entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The genre name.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value instanceof Genres)
        {
            return ((Genres) value).getGenreName();
        }
        else
        {
            return "";
        }
    }

}
