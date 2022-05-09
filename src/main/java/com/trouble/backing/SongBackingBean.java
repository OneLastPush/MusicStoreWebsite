package com.trouble.backing;

import com.trouble.JPAController.SongsJpaController;
import com.trouble.entities.Songs;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Backing Bean for a Songs entity.
 * 
 * @author Seaim Khan
 */
@Named("theSongs")
@SessionScoped
public class SongBackingBean implements Serializable
{

    @Inject
    private SongsJpaController songsJpaController;
    private List<Songs> songs;
    private List<Songs> filteredSongs;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    /**
     * Init method that loads a list of all Songs entities.
     */
    @PostConstruct
    public void init()
    {
        this.songs = songsJpaController.findSongsEntities();
    }
    
    /**
     * Gets a list of all songs.
     * 
     * @return The list of all Songs entities.
     */
    public List<Songs> getSongs(){
        return songs;
    }
    
    /**
     * Sets a list of all songs.
     * 
     * @param songs The inputted list of Songs entities.
     */
    public void setSongs(List<Songs> songs){
        this.songs = songs;
    }
    
    /**
     * Gets a list of filtered songs.
     * 
     * @return The list of filtered Songs entities.
     */
    public List<Songs> getFilteredSongs(){
        return filteredSongs;
    }
    
    /**
     * Sets a list of filtered songs.
     * 
     * @param filteredSongs The inputted list of filtered Songs entities.
     */
    public void setFilteredSongs(List<Songs> filteredSongs){
        this.filteredSongs = filteredSongs;
    }
    
    /**
     * Gets a song's total sales value.
     * 
     * @param id The song's ID.
     * @return The song's total sales value.
     */
    public BigDecimal getSongTotalSales(Integer id){
       Query query = em.createNativeQuery(
            "SELECT SUM(sold_price) AS track_total_sales FROM invoice_details_songs " +
            "INNER JOIN invoices ON invoices.id = invoice_details_songs.sale_id " +
            "WHERE invoice_details_songs.inventory_id = ?")
                .setParameter(1, id);
        if(query.getSingleResult() == null)
            return new BigDecimal("0.00");
        else
            return (BigDecimal)query.getSingleResult();
    }
}
