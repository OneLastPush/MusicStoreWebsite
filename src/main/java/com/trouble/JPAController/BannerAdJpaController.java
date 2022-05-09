package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.BannerAd;
import com.trouble.entities.BannerAd_;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
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
public class BannerAdJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    public void create(BannerAd bannerAd) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(bannerAd);
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

    public void edit(BannerAd bannerAd) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            bannerAd = em.merge(bannerAd);
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
                Integer id = bannerAd.getId();
                if (findBannerAd(id) == null)
                {
                    throw new NonexistentEntityException("The bannerAd with id " + id + " no longer exists.");
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
            BannerAd bannerAd;
            try
            {
                bannerAd = em.getReference(BannerAd.class, id);
                bannerAd.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The bannerAd with id " + id + " no longer exists.", enfe);
            }
            em.remove(bannerAd);
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

    public List<BannerAd> findBannerAdEntities()
    {
        return findBannerAdEntities(true, -1, -1);
    }

    public List<BannerAd> findBannerAdEntities(int maxResults, int firstResult)
    {
        return findBannerAdEntities(false, maxResults, firstResult);
    }

    private List<BannerAd> findBannerAdEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(BannerAd.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public BannerAd findBannerAd(Integer id)
    {
        return em.find(BannerAd.class, id);
    }

    public int getBannerAdCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<BannerAd> rt = cq.from(BannerAd.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public BannerAd findCurrentAd(){
        try{
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<BannerAd> e = cq.from(BannerAd.class);
            cq.where(cb.equal(e.get(BannerAd_.display), true));
            cq.select(e);
            TypedQuery<BannerAd> query = em.createQuery(cq);
            return query.getSingleResult();
        }
        catch(Exception e){
            return new BannerAd();
        }
    }
}
