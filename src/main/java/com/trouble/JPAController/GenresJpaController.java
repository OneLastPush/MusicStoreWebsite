package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Genres;
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
public class GenresJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public GenresJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param genre
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Genres genres) throws RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            em.persist(genres);
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
     * @param genre
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Genres genres) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            genres = em.merge(genres);
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
                Integer id = genres.getId();
                if (findGenres(id) == null)
                {
                    throw new NonexistentEntityException("The genres with id " + id + " no longer exists.");
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
            Genres genres;
            try
            {
                genres = em.getReference(Genres.class, id);
                genres.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The genres with id " + id + " no longer exists.", enfe);
            }
            em.remove(genres);
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
    public List<Genres> findGenresEntities()
    {
        return findGenresEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Genres> findGenresEntities(int maxResults, int firstResult)
    {
        return findGenresEntities(false, maxResults, firstResult);
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
    private List<Genres> findGenresEntities(boolean all, int maxResults, int firstResult)
    {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Genres.class));
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
    public Genres findGenres(Integer id)
    {
        return em.find(Genres.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getGenresCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Genres> rt = cq.from(Genres.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
}
