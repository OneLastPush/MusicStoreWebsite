package com.trouble.backing;

import com.trouble.JPAController.ReviewsJpaController;
import com.trouble.entities.Reviews;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing Bean for a Reviews entity.
 * 
 * @author Seaim Khan
 */
@Named("theReviews")
@SessionScoped
public class ReviewBackingBean implements Serializable{
    
    @Inject
    private ReviewsJpaController reviewsJpaController;
    
    /**
     * Gets a list of all reviews.
     * 
     * @return The list of all Reviews entities.
     */
    public List<Reviews> getReviews(){
        return reviewsJpaController.findReviewsEntities();
    }
}
