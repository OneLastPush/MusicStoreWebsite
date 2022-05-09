package com.trouble.backing;

import com.trouble.JPAController.AlbumsJpaController;
import com.trouble.entities.Albums;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Backing Bean for an Albums entity.
 * 
 * @author Seaim Khan
 */
@Named("theAlbums")
@SessionScoped
public class AlbumBackingBean implements Serializable
{

    @Inject
    private AlbumsJpaController albumsJpaController;

    private List<Albums> albums;
    private List<Albums> filteredAlbums;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    /**
     * Init method that loads a list of all Albums entities.
     */
    @PostConstruct
    public void init()
    {
        this.albums = albumsJpaController.findAlbumsEntities();
    }
    
    /**
     * A complete method for PrimeFaces autocomplete that finds albums that
     * start with a particular input.
     * 
     * @param query The inputted query.
     * @return The list of suggested Albums entities based on matching input.
     */
    public List<Albums> completeAlbum(String query){
        List<Albums> suggested = new ArrayList<>();
        for (Albums a : albums)
        {
            if (a.getAlbumTitle().startsWith(query))
            {
                suggested.add(a);
            }
        }
        return suggested;
    }
    
    /**
     * Gets a list of albums.
     * 
     * @return The list of albums.
     */
    public List<Albums> getAlbums() {
        return albums;
    }
    
    /**
     * Gets a list of albums.
     * 
     * @param albums The inputted list of Albums entities.
     */
    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

    /**
     * Gets a list of filtered albums.
     * 
     * @return The list of filtered albums.
     */
    public List<Albums> getFilteredAlbums() {
        return filteredAlbums;
    }

    /**
     * Sets a list of filtered albums.
     * 
     * @param filteredAlbums The inputted list of Albums entities.
     */
    public void setFilteredAlbums(List<Albums> filteredAlbums) {
        this.filteredAlbums = filteredAlbums;
    }
    
    /**
     * Gets an album's total sales value.
     * 
     * @param id An album's ID.
     * @return The album's total sales value.
     */
    public BigDecimal getAlbumTotalSales(Integer id){
        Query query = em.createNativeQuery(
            "SELECT SUM(sold_price) AS album_total_sales FROM invoice_details_album " +
            "INNER JOIN invoices ON invoices.id = invoice_details_album.sale_id " +
            "WHERE invoice_details_album.inventory_id = ?")
                .setParameter(1, id);
        if(query.getSingleResult() == null)
            return new BigDecimal("0.00");
        else
            return (BigDecimal)query.getSingleResult();
    }
}
