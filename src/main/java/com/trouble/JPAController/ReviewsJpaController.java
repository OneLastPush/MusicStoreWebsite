package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Reviews;
import com.trouble.entities.Reviews_;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;

/**
 *
 * @author Frank Birikundavyi
 */
public class ReviewsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public ReviewsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param review
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Reviews reviews) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(reviews);
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
     * @param review
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Reviews reviews) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            reviews = em.merge(reviews);
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
                Integer id = reviews.getId();
                if (findReviews(id) == null)
                {
                    throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.");
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
            Reviews reviews;
            try
            {
                reviews = em.getReference(Reviews.class, id);
                reviews.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The reviews with id " + id + " no longer exists.", enfe);
            }
            em.remove(reviews);
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
    public List<Reviews> findReviewsEntities()
    {
        return findReviewsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Reviews> findReviewsEntities(int maxResults, int firstResult)
    {
        return findReviewsEntities(false, maxResults, firstResult);
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
    private List<Reviews> findReviewsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Reviews.class));
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
    public Reviews findReviews(Integer id)
    {
        return em.find(Reviews.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getReviewsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Reviews> rt = cq.from(Reviews.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Method retrieve all the reviews from a specific songs
     *
     * @param songId
     *
     * @return
     */
    public List<Reviews> findReviewsFromSong(int songId)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Reviews> review = cq.from(Reviews.class);
        cq.where(cb.equal(review.get(Reviews_.inventoryId), songId));
        TypedQuery<Reviews> query = em.createQuery(cq);
        List<Reviews> list = query.getResultList();
        return list;
    }

}
