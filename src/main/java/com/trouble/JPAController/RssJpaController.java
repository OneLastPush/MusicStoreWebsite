package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.Rss;
import com.trouble.entities.Rss_;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
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
 * @author Seaim Khan
 */
public class RssJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    public RssJpaController()
    {
    }

    public void create(Rss rss) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(rss);
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

    public void edit(Rss rss) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            rss = em.merge(rss);
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
                Integer id = rss.getId();
                if (findRss(id) == null)
                {
                    throw new NonexistentEntityException("The rss with id " + id + " no longer exists.");
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
            Rss rss;
            try
            {
                rss = em.getReference(Rss.class, id);
                rss.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The rss with id " + id + " no longer exists.", enfe);
            }
            em.remove(rss);
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

    public List<Rss> findRssEntities()
    {
        return findRssEntities(true, -1, -1);
    }

    public List<Rss> findRssEntities(int maxResults, int firstResult)
    {
        return findRssEntities(false, maxResults, firstResult);
    }

    private List<Rss> findRssEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Rss.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Rss findRss(Integer id)
    {
        return em.find(Rss.class, id);
    }

    public int getRssCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Rss> rt = cq.from(Rss.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public Rss findByFeed(String feed)
    {
        TypedQuery<Rss> query = em.createNamedQuery("Rss.findByFeed", Rss.class);
        query.setParameter("feed", feed);
        Rss result = query.getSingleResult();
        return result;
    }

    public Rss findCurrentFeed()
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Rss> e = cq.from(Rss.class);
        cq.where(cb.equal(e.get(Rss_.currentRss), true));
        cq.select(e);
        TypedQuery<Rss> query = em.createQuery(cq);
        
        return query.getSingleResult(); 
    }  
}
