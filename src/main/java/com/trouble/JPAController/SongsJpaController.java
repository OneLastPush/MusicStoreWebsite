package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Albums;
import com.trouble.entities.Artists;
import com.trouble.entities.Artists_;
import com.trouble.entities.Songs;
import com.trouble.entities.Songs_;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import javax.persistence.TypedQuery;

/**
 *
 * @author Dimitri Spyropoulos, Frank Birikundavyi
 */
@Named
@RequestScoped
public class SongsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    /**
     * Default constructor
     */
    public SongsJpaController()
    {
    }

    /**
     * Take a new or detached entity and add it as a new record in the table
     *
     * @param songs
     *
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void create(Songs songs) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(songs);
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
     * @param songs
     *
     * @throws NonexistentEntityException
     * @throws RollbackFailureException
     * @throws Exception
     */
    public void edit(Songs songs) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            songs = em.merge(songs);
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
                Integer id = songs.getId();
                if (findSongs(id) == null)
                {
                    throw new NonexistentEntityException("The songs with id " + id + " no longer exists.");
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
            Songs songs;
            try
            {
                songs = em.getReference(Songs.class, id);
                songs.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The songs with id " + id + " no longer exists.", enfe);
            }
            em.remove(songs);
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
    public List<Songs> findSongsEntities()
    {
        return findSongsEntities(true, -1, -1);
    }

    /**
     * Return some of the records from the table. Useful for paginating.
     *
     * @param maxResults
     * @param firstResult
     *
     * @return
     */
    public List<Songs> findSongsEntities(int maxResults, int firstResult)
    {
        return findSongsEntities(false, maxResults, firstResult);
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
    private List<Songs> findSongsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Songs.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    /**
     * Return all songs from specific album
     *
     * @param album
     *
     * @return
     */
    public List<Songs> findSongsFromAlbum(Albums album)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        cq.where(cb.equal(song.get(Songs_.albumId), album.getId()));
        cq.orderBy(cb.asc(song.get(Songs_.trackNumber)));
        TypedQuery<Songs> query = em.createQuery(cq);
        List<Songs> songs = query.getResultList();
        return songs;
       
    }

    public Songs findSongs(Integer id)
    {
        return em.find(Songs.class, id);
    }

    public List<Songs> searchSongs(String search)
    {
        List<Songs> songs = em.createNamedQuery("Songs.searchByKeyword")
                .setParameter("trackTitle", search)
                .getResultList();
        return songs;
    }

    public int getSongsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Songs> rt = cq.from(Songs.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Return artist name from an id
     *
     * @param artistId
     *
     * @return
     */
    public String findArtistName(int artistId)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Artists> artist = cq.from(Artists.class);
        cq.where(cb.equal(artist.get(Artists_.id), artistId));
        TypedQuery<Artists> query = em.createQuery(cq);
        List<Artists> list = query.getResultList();
        return list.get(0).getArtist();
    }


    /*
     * public List<Songs> findSongsByDate(String from, String to){
     * CriteriaBuilder cb = em.getCriteriaBuilder(); CriteriaQuery cq =
     * cb.createQuery(); Root<Songs> song = cq.from(Songs.class);
     * ParameterExpression<Date> d = cb.parameter(Date.class);
     * cq.where(cb.between(d,song.<Date>get())); TypedQuery<Songs> query =
     * em.createQuery(cq); List<Songs> list = query.getResultList(); return
     * list; }
     */
    public List<Songs> findSongsByTitle(String search)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Songs> song = cq.from(Songs.class);
        cq.where(cb.like(song.get(Songs_.trackTitle), "%" + search + "%"));
        TypedQuery<Songs> query = em.createQuery(cq);
        List<Songs> list = query.getResultList();
        return list;
    }

    /**
     * Returns all tracks that a client has purchased.
     * 
     * @param clientId
     * @return List of the titles of tracks purchased.
     */
    public List<Object[]> getTracksPurchased(int clientId)
    {
        Query query = em.createNativeQuery(
                "SELECT distinct songs.track_title, songs.song_length FROM songs INNER JOIN "
                + "invoice_details_songs ON songs.id = invoice_details_songs.inventory_id "
                + "INNER JOIN invoices ON invoice_details_songs.sale_id = invoices.id "
                + "WHERE invoices.client_id = ?")
                .setParameter(1, clientId);
        return query.getResultList();
    }

    /**
     * Returns all tracks part of an album purchased by the client.
     * 
     * @param clientId
     * @return List of the titles from albums purchased.
     */
    public List<Object[]> getAlbumsPurchased(int clientId)
    {
        Query query = em.createNativeQuery(
                "SELECT songs.track_title, songs.song_length, albums.album_title "
                + "FROM songs INNER JOIN albums ON songs.album_id = albums.id WHERE "
                + "songs.album_id IN ( SELECT inventory_id FROM invoice_details_album "
                + "INNER JOIN invoices ON invoice_details_album.sale_id = invoices.id "
                + "WHERE invoices.client_id = ? )")
                .setParameter(1, clientId);
        return query.getResultList();
    }

}
