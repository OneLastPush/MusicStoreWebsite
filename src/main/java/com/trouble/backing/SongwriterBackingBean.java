package com.trouble.backing;

import com.trouble.JPAController.SongwritersJpaController;
import com.trouble.entities.Songwriters;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for a Songwriters entity.
 * 
 * @author Seaim Khan
 */
@Named("theSongwriters")
@SessionScoped
public class SongwriterBackingBean implements Serializable{
    
    @Inject
    private SongwritersJpaController songwritersJpaController;
    
    /**
     * Gets a list of all songwriters.
     * 
     * @return The list of all Songwriters entities.
     */
    public List<Songwriters> getSongwriters(){
        return songwritersJpaController.findSongwritersEntities();
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds songwriters
     * that start with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Songwriters entities based on matching 
     *         input.
     */
    public List<Songwriters> completeSongwriter(String query){
        List<Songwriters> songwriters = getSongwriters();
        List<Songwriters> suggested = new ArrayList<>();
        for (Songwriters s : songwriters)
        {
            if (s.getSongwriter().startsWith(query))
            {
                suggested.add(s);
            }
        }
        return suggested;
    }
}
