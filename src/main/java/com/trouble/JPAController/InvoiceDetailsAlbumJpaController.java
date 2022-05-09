package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.InvoiceDetailsAlbum;
import java.io.Serializable;
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
public class InvoiceDetailsAlbumJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public InvoiceDetailsAlbumJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param InvoiceDetailsAlbum
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(InvoiceDetailsAlbum invoiceDetailsAlbum) throws RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            em.persist(invoiceDetailsAlbum);
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

    /**
     * Take a detached entity and update the matching record in the table
     *
     * @param InvoiceDetailsAlbum
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(InvoiceDetailsAlbum invoiceDetailsAlbum) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            invoiceDetailsAlbum = em.merge(invoiceDetailsAlbum);
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
                Integer id = invoiceDetailsAlbum.getId();
                if (findInvoiceDetailsAlbum(id) == null)
                {
                    throw new NonexistentEntityException("The invoiceDetailsAlbum with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }
    }

    /**
     * Delete the record that matched the primary key. Verify that the record
     * exists before deleting it.
     *
     * @param id
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            InvoiceDetailsAlbum invoiceDetailsAlbum;
            try
            {
                invoiceDetailsAlbum = em.getReference(InvoiceDetailsAlbum.class, id);
                invoiceDetailsAlbum.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The invoiceDetailsAlbum with id " + id + " no longer exists.", enfe);
            }
            em.remove(invoiceDetailsAlbum);
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

    /**
     * Return all the records in the table
     *
     * @return
     */
    public List<InvoiceDetailsAlbum> findInvoiceDetailsAlbumEntities()
    {
        return findInvoiceDetailsAlbumEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<InvoiceDetailsAlbum> findInvoiceDetailsAlbumEntities(int maxResults, int firstResult)
    {
        return findInvoiceDetailsAlbumEntities(false, maxResults, firstResult);
    }

    /**
     * Either find all or find a group of users
     *
     * @param all         True means find all, false means find subset
     * @param maxResults  Number of records to find
     * @param firstResult Record number to start returning records
     *
     * @return
     */
    private List<InvoiceDetailsAlbum> findInvoiceDetailsAlbumEntities(boolean all, int maxResults, int firstResult)
    {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(InvoiceDetailsAlbum.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    /**
     * Find a record by primary key
     *
     * @param id
     *
     * @return
     */
    public InvoiceDetailsAlbum findInvoiceDetailsAlbum(Integer id)
    {
        return em.find(InvoiceDetailsAlbum.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getInvoiceDetailsAlbumCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<InvoiceDetailsAlbum> rt = cq.from(InvoiceDetailsAlbum.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Returns the total purchases of a specific album within a defined date range.
     * 
     * @param albumId
     * @param startDate
     * @param endDate
     * @return Number of albums sold, the purchase total and the album's removal status.
     */
    public List<Object[]> getSalesByAlbum(int albumId, Date startDate, Date endDate)
    {
        Query query = em.createNativeQuery(
            "SELECT COUNT(inventory_id), SUM(sold_price) AS album_total_sales, albums.removal_status FROM invoice_details_album " +
            "INNER JOIN invoices ON invoices.id = invoice_details_album.sale_id " +
            "INNER JOIN albums ON albums.id = invoice_details_album.inventory_id " +
            "WHERE invoice_details_album.inventory_id = ? AND invoices.sale_date BETWEEN ? AND ?")
                .setParameter(1, albumId)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        return query.getResultList();
    }
}
