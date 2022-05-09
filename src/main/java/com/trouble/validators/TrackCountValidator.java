package com.trouble.validators;

import com.trouble.JPAController.AlbumsJpaController;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Validator class for creation of a track in database.
 *
 * @author Seaim Khan
 */
@Named
public class TrackCountValidator implements Validator
{

    @Inject
    private AlbumsJpaController ajc;
    
    /**
     * Validates if a track number is within a track count.
     * 
     * @param context A FacesContext object.
     * @param component A UIComponent object.
     * @param value The inputted value.
     * @throws ValidatorException if the track number is not within the track
     *                            count.
     */
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException
    {
        Integer trackNumber = Integer.parseInt(value.toString());
        ExternalContext ec = context.getExternalContext();
        String albumTitle = null;

        if (!ec.getRequestParameterMap().get("form:trackAlbum_input").isEmpty())
        {
            albumTitle = ec.getRequestParameterMap().get("form:trackAlbum_input");
            Integer trackCount = ajc.findTrackCount(albumTitle);
            if (trackNumber > trackCount)
            {
                System.out.print("GREATERTHANCOUNT: Track number is invalid!");
                throw new ValidatorException(new FacesMessage(
                        "Track number must be less than track count of "
                        + trackCount + "!"));
            }
        }
        else
        {
            System.out.print("ALBUMTITLEERROR: Album title cannot be null or empty!");
            throw new ValidatorException(new FacesMessage("Album title must contain a value."));
        }
    }
}
