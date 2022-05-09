package com.trouble.backing;

import com.trouble.JPAController.SurveyChoicesJpaController;
import com.trouble.JPAController.SurveyQuestionsJpaController;
import com.trouble.entities.SurveyChoices;
import com.trouble.entities.SurveyQuestions;
import com.trouble.entities.SurveyQuestions_;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * Backing Bean for the survey entities.
 * 
 * @author Seaim Khan
 */
@Named("theSurveys")
@SessionScoped
public class SurveyBackingBean implements Serializable{
    
    @Inject
    private SurveyQuestionsJpaController surveyQuestionsJpaController;

    @Inject
    private SurveyChoicesJpaController surveyChoicesJpaController;
    
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;
    
    /**
     * Gets a list of all surveys.
     * 
     * @return The list of all SurveyQuestions entities.
     */
    public List<SurveyQuestions> getSurveys(){
        return surveyQuestionsJpaController.findSurveyQuestionsEntities();
    }
    
    /**
     * Gets a list of all survey choices.
     * 
     * @return The list of all SurveyChoices entities.
     */
    public List<SurveyChoices> getSurveyChoices(){
        return surveyChoicesJpaController.findSurveyChoicesEntities();
    }
    
    /**
     * Gets the current survey to be displayed on the front page.
     * 
     * @return The SurveyQuestions entity representing the current survey.
     */
    public SurveyQuestions getCurrentSurveyQuestion(){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();               
        Root<SurveyQuestions> rt = cq.from(SurveyQuestions.class);
        cq.where(cb.equal(rt.get(SurveyQuestions_.currentQuestion), true));
        cq.select(rt);
        TypedQuery<SurveyQuestions> query = em.createQuery(cq);
        
        return query.getSingleResult();     
    }
}
