package com.trouble.backingbean;

import com.trouble.entities.Albums;
import com.trouble.JPAController.AlbumsJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank Birikundavyi
 */
@Named("albumBacking")
@RequestScoped
public class AlbumBackingBean implements Serializable
{

    @Inject
    private AlbumsJpaController albumsJpaController;

    private Albums album;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return album -entity
     */
    public Albums getAlbum()
    {
        if (album == null)
        {
            album = new Albums();
        }
        return album;
    }

    /**
     * Get list of albums
     *
     * @return
     */
    public List<Albums> getAllAlbums()
    {

        return albumsJpaController.findAlbumsEntities();
    }

    /**
     * Get an album from its id
     *
     * @param albumId
     *
     * @return
     */
    public Albums getAlbumById(int albumId)
    {
        return albumsJpaController.findAlbums(albumId);
    }
}
