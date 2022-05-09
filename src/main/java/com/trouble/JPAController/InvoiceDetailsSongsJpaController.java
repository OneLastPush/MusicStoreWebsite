package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.InvoiceDetailsSongs;
import com.trouble.entities.InvoiceDetailsSongs_;
import com.trouble.entities.Invoices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Dimitri Spyropoulos
 */
@Named
@RequestScoped
public class InvoiceDetailsSongsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public InvoiceDetailsSongsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param InvoiceDetailsSongs
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(InvoiceDetailsSongs invoiceDetailsSongs) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(invoiceDetailsSongs);
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
     * @param InvoiceDetailsSongs
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(InvoiceDetailsSongs invoiceDetailsSongs) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            invoiceDetailsSongs = em.merge(invoiceDetailsSongs);
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
                Integer id = invoiceDetailsSongs.getId();
                if (findInvoiceDetailsSongs(id) == null)
                {
                    throw new NonexistentEntityException("The invoiceDetailsSongs with id " + id + " no longer exists.");
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
            InvoiceDetailsSongs invoiceDetailsSongs;
            try
            {
                invoiceDetailsSongs = em.getReference(InvoiceDetailsSongs.class, id);
                invoiceDetailsSongs.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The invoiceDetailsSongs with id " + id + " no longer exists.", enfe);
            }
            em.remove(invoiceDetailsSongs);
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
    public List<InvoiceDetailsSongs> findInvoiceDetailsSongsEntities()
    {
        return findInvoiceDetailsSongsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<InvoiceDetailsSongs> findInvoiceDetailsSongsEntities(int maxResults, int firstResult)
    {
        return findInvoiceDetailsSongsEntities(false, maxResults, firstResult);
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
    private List<InvoiceDetailsSongs> findInvoiceDetailsSongsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(InvoiceDetailsSongs.class));
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
    public InvoiceDetailsSongs findInvoiceDetailsSongs(Integer id)
    {
        return em.find(InvoiceDetailsSongs.class, id);
    }

    public int getInvoiceDetailsSongsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<InvoiceDetailsSongs> rt = cq.from(InvoiceDetailsSongs.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Returns the total purchases of a specific track within a defined date range.
     * 
     * @param id
     * @param startDate
     * @param endDate
     * @return Number of tracks sold, the purchase total and the track's removal status.
     */
    public List<Object[]> getSalesByTrack (int id, Date startDate, Date endDate){
        Query query = em.createNativeQuery(
            "SELECT COUNT(inventory_id), SUM(sold_price), songs.removal_status FROM invoice_details_songs " +
            "INNER JOIN invoices ON invoices.id = invoice_details_songs.sale_id " +
            "INNER JOIN songs ON songs.id = invoice_details_songs.inventory_id " +
            "WHERE invoice_details_songs.inventory_id = ? AND invoices.sale_date BETWEEN ? AND ?")
                .setParameter(1, id)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        return query.getResultList();
    }
    
    public List<InvoiceDetailsSongs> getSongsForInvoice(Invoices sale_id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<InvoiceDetailsSongs> ids = cq.from(InvoiceDetailsSongs.class);
        cq.where(cb.equal(ids.get(InvoiceDetailsSongs_.saleId), sale_id), 
                cb.equal(ids.get(InvoiceDetailsSongs_.removed), false));
        TypedQuery<InvoiceDetailsSongs> query = em.createQuery(cq);
        List<InvoiceDetailsSongs> result = (List<InvoiceDetailsSongs>)query.getResultList();
        if(result.isEmpty() || result == null)
            return new ArrayList<InvoiceDetailsSongs>();
        else
            return result;
    }
    
    public List<InvoiceDetailsSongs> getRemovedSongsForInvoice(Invoices sale_id){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<InvoiceDetailsSongs> ids = cq.from(InvoiceDetailsSongs.class);
        cq.where(cb.equal(ids.get(InvoiceDetailsSongs_.saleId), sale_id), 
                cb.equal(ids.get(InvoiceDetailsSongs_.removed), true));
        TypedQuery<InvoiceDetailsSongs> query = em.createQuery(cq);
        List<InvoiceDetailsSongs> result = (List<InvoiceDetailsSongs>)query.getResultList();
        if(result.isEmpty() || result == null)
            return new ArrayList<InvoiceDetailsSongs>();
        else
            return result;
    }
}
