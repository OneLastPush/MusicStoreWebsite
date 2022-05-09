package com.trouble.converters;

import com.trouble.backing.ArtistBackingBean;
import com.trouble.entities.Artists;
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
public class ArtistNameConverter implements Converter
{

    @Inject
    private ArtistBackingBean artistsController;
    
    private List<Artists> artists;
    
    /**
     * Init method that loads a list of all Artists entities.
     */
    @PostConstruct
    public void init(){
        this.artists = artistsController.getArtists();    
    }
    
    /**
     * Overridden getAsObject method for an Artists entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Artists entity object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value.trim().equals("")) {
            return null;
        } else {
            try {
                String name = value;
                for (Artists a : artists) {
                    if (a.getArtist().equals(name)) {
                        return a;
                    }
                }
            } catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid artist!"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for an Artists entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The artist name.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Artists) {
            return ((Artists) value).getArtist();
        } else {
            return "";
        }
    }

}
