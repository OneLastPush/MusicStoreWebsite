package com.trouble.converters;

import com.trouble.backing.LabelBackingBean;
import com.trouble.entities.Labels;
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
 * A Converter class for a Labels entity.
 * 
 * @author Seaim Khan
 */
@Named
public class LabelNameConverter implements Converter
{

    @Inject
    private LabelBackingBean labelsController;
    
    private List<Labels> labels;
    
    /**
     * Init method that loads a list of all Labels entities.
     */
    @PostConstruct
    public void init(){
        this.labels = labelsController.getLabels();    
    }
    
    /**
     * Overridden getAsObject method for a Labels entity meant for input.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The Labels entity object.
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
                for (Labels l : labels)
                {
                    if (l.getLabelName().equals(name))
                    {
                        return l;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Not a valid recording label!", "Conversion Error"));
            }
        }
        return null;
    }

    /**
     * Overridden getAsString method for a Labels entity meant for output.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @return The recording label name.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        if (value instanceof Labels)
        {
            return ((Labels) value).getLabelName();
        }
        else
        {
            return "";
        }
    }

}
