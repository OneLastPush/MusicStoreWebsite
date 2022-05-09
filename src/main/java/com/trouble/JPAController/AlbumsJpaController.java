package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Albums;
import com.trouble.entities.Albums_;
import java.io.Serializable;
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
 * @author Frank Birikundavyi
 */
@Named
@RequestScoped
public class AlbumsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    /**
     * Default constructor
     */
    public AlbumsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param album
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Albums albums) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(albums);
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
     * @param album
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Albums albums) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            albums = em.merge(albums);
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
                Integer id = albums.getId();
                if (findAlbums(id) == null)
                {
                    throw new NonexistentEntityException("The albums with id " + id + " no longer exists.");
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

            Albums albums;
            try
            {
                albums = em.getReference(Albums.class, id);
                albums.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The albums with id " + id + " no longer exists.", enfe);
            }
            em.remove(albums);
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
    public List<Albums> findAlbumsEntities()
    {
        return findAlbumsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Albums> findAlbumsEntities(int maxResults, int firstResult)
    {
        return findAlbumsEntities(false, maxResults, firstResult);
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
    private List<Albums> findAlbumsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Albums.class));
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
    public Albums findAlbums(Integer id)
    {
        return em.find(Albums.class, id);
    }

    /**
     * Return the number of records in the table
     *
     * @return
     */
    public int getAlbumsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Albums> rt = cq.from(Albums.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Return the link of the album's cover image
     *
     * @param albumId
     *
     * @return
     */
    public String findAlbumCover(int albumId)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> album = cq.from(Albums.class);
        cq.where(cb.equal(album.get(Albums_.id), albumId));
        TypedQuery<Albums> query = em.createQuery(cq);
        Albums result = query.getSingleResult();
        return result.getAlbumCover();
    }

    /**
     * Return an album by its id
     *
     * @param albumId
     *
     * @return
     */
    public Albums findAlbumById(int albumId)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> album = cq.from(Albums.class);
        cq.where(cb.equal(album.get(Albums_.id), albumId));
        TypedQuery<Albums> query = em.createQuery(cq);
        Albums result = query.getSingleResult();
        return result;
    }

    /**
     * Returns an album by its name.
     *
     * @param albumTitle The album title of the entity.
     *
     * @return The Albums entity containing the provided album title.
     */
    public Albums findAlbumByTitle(String albumTitle)
    {
        TypedQuery<Albums> query
                = em.createNamedQuery("Albums.findByAlbumTitle", Albums.class);
        query.setParameter("albumTitle", albumTitle);
        Albums result = query.getSingleResult();
        return result;
    }

    public Integer findTrackCount(String albumTitle)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Albums> album = cq.from(Albums.class);
       cq.where(cb.equal(album.get(Albums_.albumTitle), albumTitle));
        TypedQuery<Albums> query = em.createQuery(cq);
        Albums result = query.getSingleResult();
        return result.getTrackCount();
    }
}
