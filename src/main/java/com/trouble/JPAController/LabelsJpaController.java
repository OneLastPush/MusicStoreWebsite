package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Labels;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Frank Birikundavyi
 */
public class LabelsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public LabelsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param label
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Labels labels) throws RollbackFailureException, Exception
    {

        try
        {
            utx.begin();

            em.persist(labels);
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
     * @param label
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Labels labels) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();

            labels = em.merge(labels);
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
                Integer id = labels.getId();
                if (findLabels(id) == null)
                {
                    throw new NonexistentEntityException("The labels with id " + id + " no longer exists.");
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
            Labels labels;
            try
            {
                labels = em.getReference(Labels.class, id);
                labels.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The labels with id " + id + " no longer exists.", enfe);
            }
            em.remove(labels);
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
    public List<Labels> findLabelsEntities()
    {
        return findLabelsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Labels> findLabelsEntities(int maxResults, int firstResult)
    {
        return findLabelsEntities(false, maxResults, firstResult);
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
    private List<Labels> findLabelsEntities(boolean all, int maxResults, int firstResult)
    {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Labels.class));
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
    public Labels findLabels(Integer id)
    {

        return em.find(Labels.class, id);

    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getLabelsCount()
    {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Labels> rt = cq.from(Labels.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();

    }

}
