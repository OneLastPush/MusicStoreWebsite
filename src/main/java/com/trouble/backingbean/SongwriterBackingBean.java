package com.trouble.backingbean;

import com.trouble.entities.Songwriters;
import com.trouble.JPAController.SongwritersJpaController;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("songwriterBacking")
@RequestScoped
public class SongwriterBackingBean implements Serializable
{

    @Inject
    private SongwritersJpaController songwritersJpaController;

    private Songwriters songwriter;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return songwriter -entity
     */
    public Songwriters getSongwriter()
    {
        if (songwriter == null)
        {
            songwriter = new Songwriters();
        }
        return songwriter;
    }

    /**
     * Get list of songwriter
     *
     * @return
     */
    public List<Songwriters> getAllSongwriters()
    {

        return songwritersJpaController.findSongwritersEntities();
    }
}
