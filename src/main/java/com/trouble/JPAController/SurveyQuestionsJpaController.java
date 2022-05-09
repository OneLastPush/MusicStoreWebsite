package com.trouble.JPAController;

import com.trouble.JPAController.exceptions.NonexistentEntityException;
import com.trouble.JPAController.exceptions.RollbackFailureException;
import com.trouble.entities.SurveyQuestions;
import java.io.Serializable;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;

/**
 *
 * @author Seaim Khan
 */
public class SurveyQuestionsJpaController implements Serializable
{

    @Resource
    private UserTransaction utx;

    @PersistenceContext
    private EntityManager em;

    public SurveyQuestionsJpaController()
    {
    }

    public void create(SurveyQuestions surveyQuestions) throws RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            em.persist(surveyQuestions);
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

    public void edit(SurveyQuestions surveyQuestions) throws NonexistentEntityException, RollbackFailureException, Exception
    {
        try
        {
            utx.begin();
            surveyQuestions = em.merge(surveyQuestions);
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
                Integer id = surveyQuestions.getId();
                if (findSurveyQuestions(id) == null)
                {
                    throw new NonexistentEntityException("The surveyQuestions with id " + id + " no longer exists.");
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
            SurveyQuestions surveyQuestions;
            try
            {
                surveyQuestions = em.getReference(SurveyQuestions.class, id);
                surveyQuestions.getId();
            }
            catch (EntityNotFoundException enfe)
            {
                throw new NonexistentEntityException("The surveyQuestions with id " + id + " no longer exists.", enfe);
            }
            em.remove(surveyQuestions);
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

    public List<SurveyQuestions> findSurveyQuestionsEntities()
    {
        return findSurveyQuestionsEntities(true, -1, -1);
    }

    public List<SurveyQuestions> findSurveyQuestionsEntities(int maxResults, int firstResult)
    {
        return findSurveyQuestionsEntities(false, maxResults, firstResult);
    }

    private List<SurveyQuestions> findSurveyQuestionsEntities(boolean all, int maxResults, int firstResult)
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(SurveyQuestions.class));
        Query q = em.createQuery(cq);
        if (!all)
        {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public SurveyQuestions findSurveyQuestions(Integer id)
    {
        return em.find(SurveyQuestions.class, id);
    }

    public int getSurveyQuestionsCount()
    {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<SurveyQuestions> rt = cq.from(SurveyQuestions.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    public List<SurveyQuestions> findByQuestion(String question)
    {
        TypedQuery<SurveyQuestions> query
                = em.createNamedQuery("SurveyQuestions.findByQuestion", SurveyQuestions.class);
        query.setParameter("question", question);
        List<SurveyQuestions> results = query.getResultList();
        return results;
    }
}
