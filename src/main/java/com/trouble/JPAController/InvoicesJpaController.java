package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Invoices;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Dimitri Spyropoulos
 */
@Named
@RequestScoped
public class InvoicesJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    public InvoicesJpaController()
    {
    }

    public void create(Invoices invoices) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(invoices);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {
                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public void edit(Invoices invoices) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            invoices = em.merge(invoices);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {
                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0)
            {
                Integer id = invoices.getId();
                if (findInvoices(id) == null)
                {
                    throw new NonexistentEntityException("The invoices with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            Invoices invoices;
            try
            {
                invoices = em.getReference(Invoices.class, id);
                invoices.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The invoices with id " + id + " no longer exists.", enfe);
            }
            em.remove(invoices);
            utx.commit();
        }
        catch (Exception ex)
        {
            try
            {
                utx.rollback();
            }
            catch (Exception re)
            {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        }
    }

    public List<Invoices> findInvoicesEntities()
    {
        return findInvoicesEntities(true, -1, -1);
    }

    public List<Invoices> findInvoicesEntities(int maxResults, int firstResult)
    {
        return findInvoicesEntities(false, maxResults, firstResult);
    }

    private List<Invoices> findInvoicesEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Invoices.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    

    public Invoices findInvoices(Integer id)
    {
        return em.find(Invoices.class, id);
    }
    
    public int getInvoicesCount() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Invoices> rt = cq.from(Invoices.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    /**
     * Returns the total combined sales of tracks and albums for a specified artist within a defined date range.
     * 
     * @param artistId
     * @param startDate
     * @param endDate
     * @return Total sales for a specific artist.
     */
    public String getSalesByArtist(int artistId, Date startDate, Date endDate){
        double result = getArtistSongSales(artistId,startDate, endDate) + getArtistAlbumSales(artistId, startDate, endDate);
        
        return String.format("%.2f", result);
    }
    
    /**
     * Returns the total sales of tracks for a specified artist within a defined date range.
     * 
     * @param artistId
     * @param startDate
     * @param endDate
     * @return Total track sales for a specific artist.
     */
    public double getArtistSongSales(int artistId, Date startDate, Date endDate){
        Query query = em.createNativeQuery(
            "SELECT SUM(invoice_details_songs.sold_price) FROM invoices " +
            "INNER JOIN invoice_details_songs ON invoice_details_songs.sale_id = invoices.id " +
            "INNER JOIN songs ON invoice_details_songs.inventory_id = songs.id " +
            "WHERE songs.artist_id = ? AND invoices.sale_date BETWEEN ? AND ?")
                .setParameter(1, artistId)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        
        BigDecimal bd = (BigDecimal) query.getSingleResult();      
        if(bd != null)
            return bd.doubleValue();
        else
            return 0.0;
    }
    
    /**
     * Returns the total sales of albums for a specified artist within a defined date range.
     * 
     * @param artistId
     * @param startDate
     * @param endDate
     * @return Total album sales for a specific artist.
     */
    public double getArtistAlbumSales(int artistId, Date startDate, Date endDate){
        Query query = em.createNativeQuery(
            "SELECT sum(ida.`sold_price`) FROM invoices INNER JOIN invoice_details_album AS ida " +
            "ON ida.sale_id = invoices.id INNER JOIN albums ON ida.inventory_id = albums.id " +
            "WHERE albums.artist_id = ? AND invoices.sale_date BETWEEN ? AND ?")
                .setParameter(1, artistId)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        
        BigDecimal bd = (BigDecimal) query.getSingleResult();     
        if(bd != null)
            return bd.doubleValue();
        else
            return 0.0;
    }
    
    /**
     * Returns the total sales within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return Total amount of sales.
     */
    public double getSales(Date startDate, Date endDate){
        Query query = em.createNativeQuery(
                "SELECT sum(gross_value) FROM invoices WHERE sale_date "
                + "BETWEEN ? AND ?")
                .setParameter(1, startDate)
                .setParameter(2, endDate);

        BigDecimal bd = (BigDecimal) query.getSingleResult();
        
        if(bd != null)
            return bd.doubleValue();
        else
            return 0.0;
    }

    /**
     * Returns the total sales for a specified client within a defined date range.
     * 
     * @param clientId
     * @param startDate
     * @param endDate
     * @return Total sales for a specific client.
     */
    public String getSalesByClient(int clientId, Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
                "SELECT sum(gross_value) FROM invoices WHERE client_id = ? "
                + "AND sale_date BETWEEN ? AND ?")
                .setParameter(1, clientId)
                .setParameter(2, startDate)
                .setParameter(3, endDate);

        BigDecimal bd = (BigDecimal) query.getSingleResult();

        if (bd != null)
        {
            return String.format("%.2f", bd.doubleValue());
        }
        else
        {
            return String.format("%.2f", 0.0);
        }
    }
    
    /**
     * Returns the sum of the cost of the albums and cost of the tracks within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return The total cost.
     */
    public double getTotalCost(Date startDate, Date endDate){
        double result = getTotalAlbumsCost(startDate, endDate) + getTotalTracksCost(startDate, endDate);
        
        return result;
    }
    
    /**
     * Returns the total profit, within a defined date range, by subtracting the total sales from the total cost.
     * 
     * @param startDate
     * @param endDate
     * @return The total profit.
     */
    public String getProfit(Date startDate, Date endDate){
        double result = getSales(startDate, endDate) - getTotalCost(startDate, endDate);
        
        return String.format("%.2f", result);
    }
    
    /**
     * Returns the cost of all tracks sold within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return Cost of all the tracks sold.
     */
    public double getTotalTracksCost(Date startDate, Date endDate){
        Query query = em.createNativeQuery(
            "SELECT SUM(cost_price) FROM songs INNER JOIN invoice_details_songs ON " +
            "songs.id = invoice_details_songs.inventory_id INNER JOIN invoices ON " +
            "invoice_details_songs.sale_id = invoices.id WHERE invoices.sale_date " +
            "BETWEEN ? AND ?")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        
        BigDecimal bd = (BigDecimal) query.getSingleResult();     
        if(bd != null)
            return bd.doubleValue();
        else
            return 0.0;
    }
    
    /**
     * Returns the cost of all albums sold within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return Cost of all albums sold.
     */
    public double getTotalAlbumsCost(Date startDate, Date endDate){
        Query query = em.createNativeQuery(
            "SELECT SUM(cost_price) FROM albums INNER JOIN invoice_details_album " + 
            "ON albums.id = invoice_details_album.inventory_id INNER JOIN invoices " +
            "ON invoice_details_album.sale_id = invoices.id WHERE invoices.sale_date " +
            "BETWEEN ? AND ?")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        
        BigDecimal bd = (BigDecimal) query.getSingleResult();     
        if(bd != null)
            return bd.doubleValue();
        else
            return 0.0;
    }

    /**
     * Returns tracks ordered by their total sales within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return Top selling tracks and their total sales.
     */
    public List<Object[]> getTopSellingTracks(Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
            "SELECT songs.track_title, songs.removal_status, SUM(invoice_details_songs.sold_price) AS total_sales " +
            "FROM invoices INNER JOIN invoice_details_songs " +
            "ON invoice_details_songs.sale_id = invoices.id INNER JOIN songs " +
            "ON invoice_details_songs.inventory_id = songs.id " +
            "WHERE invoices.sale_date BETWEEN ? AND ? GROUP BY songs.track_title, songs.removal_status " +
            "ORDER BY total_sales DESC")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        return query.getResultList();
    }

    /**
     * Returns all the tracks that haven't sold within a defined date range.
     * 
     * @param startDate
     * @param endDate
     * @return All tracks without sales.
     */
    public List<Object[]> getTracksWithoutSales(Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
            "SELECT songs.id, songs.track_title, songs.removal_status FROM invoices LEFT OUTER JOIN invoice_details_songs " +
            "ON invoice_details_songs.sale_id = invoices.id RIGHT OUTER JOIN songs " + 
            "ON invoice_details_songs.inventory_id = songs.id WHERE invoices.sale_date " +
            "NOT BETWEEN ? AND ? OR invoices.sale_date IS NULL")
                .setParameter(1, startDate)
                .setParameter(2, endDate);
        return query.getResultList();
    }
}
