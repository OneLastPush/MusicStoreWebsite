package com.trouble.backingbean;

import com.trouble.JPAController.GenresJpaController;
import com.trouble.entities.Genres;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank
 */
@Named("genreBacking")
@RequestScoped
public class GenreBackingBean implements Serializable
{

    @Inject
    private GenresJpaController genresJpaController;

    private Genres genre;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return genre -entity
     */
    public Genres getGenre()
    {
        if (genre == null)
        {
            genre = new Genres();
        }
        return genre;
    }

    /**
     * Get list of genre
     *
     * @return
     */
    public List<Genres> getAllGenres()
    {

        return genresJpaController.findGenresEntities();
    }
}
