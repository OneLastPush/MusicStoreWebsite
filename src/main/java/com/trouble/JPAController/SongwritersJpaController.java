package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Songwriters;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
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
@Named
@RequestScoped
public class SongwritersJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public SongwritersJpaController()
    {

    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param songwriter
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Songwriters songwriters) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(songwriters);
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
     * @param songwriter
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Songwriters songwriters) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            songwriters = em.merge(songwriters);
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
                Integer id = songwriters.getId();
                if (findSongwriters(id) == null)
                {
                    throw new NonexistentEntityException("The songwriters with id " + id + " no longer exists.");
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
            Songwriters songwriters;
            try
            {
                songwriters = em.getReference(Songwriters.class, id);
                songwriters.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The songwriters with id " + id + " no longer exists.", enfe);
            }
            em.remove(songwriters);
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
    public List<Songwriters> findSongwritersEntities()
    {
        return findSongwritersEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Songwriters> findSongwritersEntities(int maxResults, int firstResult)
    {
        return findSongwritersEntities(false, maxResults, firstResult);
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
    private List<Songwriters> findSongwritersEntities(boolean all, int maxResults, int firstResult)
    {

        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Songwriters.class));
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
    public Songwriters findSongwriters(Integer id)
    {
        return em.find(Songwriters.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getSongwritersCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Songwriters> rt = cq.from(Songwriters.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
