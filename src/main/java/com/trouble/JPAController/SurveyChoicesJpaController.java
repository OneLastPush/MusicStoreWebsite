package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.PreexistingEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.SurveyChoices;
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
 * @author Dimitri Spyropoulos
 */
public class SurveyChoicesJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    public SurveyChoicesJpaController()
    {
    }

    public void create(SurveyChoices surveyChoices) throws PreexistingEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(surveyChoices);
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
            if (findSurveyChoices(surveyChoices.getId()) != null)
            {
                throw new PreexistingEntityException("SurveyChoices " + surveyChoices + " already exists.", ex);
            }
            throw ex;
        }
    }

    public void edit(SurveyChoices surveyChoices) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            surveyChoices = em.merge(surveyChoices);
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
                Integer id = surveyChoices.getId();
                if (findSurveyChoices(id) == null)
                {
                    throw new NonexistentEntityException("The surveyChoices with id " + id + " no longer exists.");
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
            SurveyChoices surveyChoices;
            try
            {
                surveyChoices = em.getReference(SurveyChoices.class, id);
                surveyChoices.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The surveyChoices with id " + id + " no longer exists.", enfe);
            }
            em.remove(surveyChoices);
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

    public List<SurveyChoices> findSurveyChoicesEntities()
    {
        return findSurveyChoicesEntities(true, -1, -1);
    }

    public List<SurveyChoices> findSurveyChoicesEntities(int maxResults, int firstResult)
    {
        return findSurveyChoicesEntities(false, maxResults, firstResult);
    }

    private List<SurveyChoices> findSurveyChoicesEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SurveyChoices.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SurveyChoices findSurveyChoices(Integer id)
    {
        return em.find(SurveyChoices.class, id);
    }

    public int getSurveyChoicesCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SurveyChoices> rt = cq.from(SurveyChoices.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
