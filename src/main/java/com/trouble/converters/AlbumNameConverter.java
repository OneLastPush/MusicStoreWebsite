package com.trouble.converters;

import com.trouble.backing.AlbumBackingBean;
import com.trouble.entities.Albums;
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
 * A Converter class for an Albums entity.
 * 
 * @author Seaim Khan
 */
@Named
public class AlbumNameConverter implements Converter
{

    @Inject
    private AlbumBackingBean albumsController;
    
    private List<Albums> albums;
    
    /**
     * Init method that loads a list of all Albums entities.
     */
    @PostConstruct
    public void init(){
        this.albums = albumsController.getAlbums();
    }
    
    /**
     * Overridden getAsObject method for an Albums entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Albums entity object.
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
                for (Albums a : albums)
                {
                    if (a.getAlbumTitle().equals(name))
                    {
                        return a;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid album!"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for an Albums entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The album title.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value instanceof Albums)
        {
            return ((Albums) value).getAlbumTitle();
        }
        else
        {
            return "";
        }
    }

}
