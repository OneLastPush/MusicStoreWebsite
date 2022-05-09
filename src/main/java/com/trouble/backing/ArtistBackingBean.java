package com.trouble.backing;

import com.trouble.JPAController.ArtistsJpaController;
import com.trouble.entities.Artists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for an Artists entity.
 * 
 * @author Seaim Khan
 */
@Named("theArtists")
@SessionScoped
public class ArtistBackingBean implements Serializable
{

    @Inject
    private ArtistsJpaController artistsJpaController;
    
    /**
     * Gets a list of all artists.
     * 
     * @return The list of all Artists entities.
     */
    public List<Artists> getArtists(){
        return artistsJpaController.findArtistsEntities();
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds artists that
     * start with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Artists entities based on matching input.
     */
    public List<Artists> completeArtist(String query){
        List<Artists> artists = getArtists();
        List<Artists> suggested = new ArrayList<>();
        for (Artists a : artists)
        {
            if (a.getArtist().startsWith(query))
            {
                suggested.add(a);
            }
        }
        return suggested;
    }
}
