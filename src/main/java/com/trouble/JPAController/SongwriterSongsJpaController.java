package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.SongwriterSongs;
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
public class SongwriterSongsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public SongwriterSongsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param songwriterSongs
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(SongwriterSongs songwriterSongs) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(songwriterSongs);
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
     * @param songwriterSongs
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(SongwriterSongs songwriterSongs) throws NonexistentEntityException, RollbackFailureException, Exception
    {

        try
        {
            utx.begin();
            songwriterSongs = em.merge(songwriterSongs);
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
                Integer id = songwriterSongs.getId();
                if (findSongwriterSongs(id) == null)
                {
                    throw new NonexistentEntityException("The songwriterSongs with id " + id + " no longer exists.");
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
            SongwriterSongs songwriterSongs;
            try
            {
                songwriterSongs = em.getReference(SongwriterSongs.class, id);
                songwriterSongs.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The songwriterSongs with id " + id + " no longer exists.", enfe);
            }
            em.remove(songwriterSongs);
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
    public List<SongwriterSongs> findSongwriterSongsEntities()
    {
        return findSongwriterSongsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<SongwriterSongs> findSongwriterSongsEntities(int maxResults, int firstResult)
    {
        return findSongwriterSongsEntities(false, maxResults, firstResult);
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
    private List<SongwriterSongs> findSongwriterSongsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SongwriterSongs.class));
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
    public SongwriterSongs findSongwriterSongs(Integer id)
    {

        return em.find(SongwriterSongs.class, id);

    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getSongwriterSongsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SongwriterSongs> rt = cq.from(SongwriterSongs.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
