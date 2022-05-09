package com.trouble.backingbean;

import com.trouble.entities.Artists;
import com.trouble.JPAController.ArtistsJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("artistBacking")
@RequestScoped
public class ArtistBackingBean implements Serializable
{

    @Inject
    private ArtistsJpaController artistsJpaController;

    private Artists artist;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return artists -entity
     */
    public Artists getArtist()
    {
        if (artist == null)
        {
            artist = new Artists();
        }
        return artist;
    }

    /**
     * Get list of artist
     *
     * @return
     */
    public List<Artists> getAllArtitsts()
    {
        return artistsJpaController.findArtistsEntities();
    }

}
