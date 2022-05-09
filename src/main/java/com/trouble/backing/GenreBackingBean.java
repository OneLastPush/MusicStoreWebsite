package com.trouble.backing;

import com.trouble.JPAController.GenresJpaController;
import com.trouble.entities.Genres;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for a Genres entity.
 * 
 * @author Seaim Khan
 */
@Named("theGenres")
@SessionScoped
public class GenreBackingBean implements Serializable{
    
    @Inject
    private GenresJpaController genresJpaController;
    
    /**
     * Gets a list of all genres.
     * 
     * @return The list of all Genres entities.
     */
    public List<Genres> getGenres(){
        return genresJpaController.findGenresEntities();
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds genres that
     * start with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Genres entities based on matching input.
     */
    public List<Genres> completeGenre(String query){
        List<Genres> genres = getGenres();
        List<Genres> suggested = new ArrayList<>();
        for (Genres g : genres)
        {
            if (g.getGenreName().startsWith(query))
            {
                suggested.add(g);
            }
        }
        return suggested;
    }
}
