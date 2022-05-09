package com.trouble.backingbean;

import com.trouble.entities.Reviews;
import com.trouble.JPAController.ReviewsJpaController;
import com.trouble.paginator.ReviewPaginator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Frank Birikundavyi
 */
@Named("reviewBacking")
@RequestScoped
public class ReviewBackingBean implements Serializable
{

    @Inject
    private ReviewsJpaController reviewsJpaController;

    private Reviews review;

    /**
     * Get the entity if not instantiated, it will
     *
     * @return review -entity
     */
    public Reviews getReview()
    {
        if (review == null)
        {
            review = new Reviews();
        }
        return review;
    }

    /**
     * Get list of review
     *
     * @return
     */
    public List<Reviews> getAllReviews()
    {
        return reviewsJpaController.findReviewsEntities();
    }
    /**
     * Retrieve a list of reviews from a specific song
     *
     * @param songId
     *
     * @return
     */
//    public List<Reviews> getReviewsFromSong(int songId){
//        List<Reviews> list = reviewsJpaController.findReviewsFromSong(songId);
//        paginator = new ReviewPaginator(list);
//        return paginator.getList();
//    }

}
