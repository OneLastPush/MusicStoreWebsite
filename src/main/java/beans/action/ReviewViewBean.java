package beans.action;

import com.trouble.JPAController.ReviewsJpaController;
import com.trouble.entities.Reviews;
import com.trouble.entities.Songs;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Frank Birikundavyi
 */
@Named("reviewView")
@RequestScoped
public class ReviewViewBean implements Serializable {

    private UIComponent mybutton;
    private Reviews review;
    private String rating;
    private String reviewText;
    private Songs song;
    @Inject
    private ReviewsJpaController reviewController;
    @PersistenceContext(unitName = "musicPU")
    private EntityManager em;

    public UIComponent getMybutton() {
        return mybutton;
    }

    public void setMybutton(UIComponent mybutton) {
        this.mybutton = mybutton;
    }

    public Songs getSong() {
        return song;
    }

    public void setSong(Songs song) {
        System.out.println("SET" + song);
        this.song = song;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        if (rating == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing rating", "Missing rating! Please rate it"));
        } else {
            this.rating = rating;
        }
    }

    public String getReviewText() {
        return reviewText;
    }

    private void setReviewParams(LoginActionBean login, Songs song) {
        if(review == null){
            review = new Reviews();
        }
        review.setRating(Integer.parseInt(rating));
        review.setReviewText(reviewText);
        review.setClientId(login.getCurrUser());
        review.setApprovalStatus(Boolean.FALSE);
        review.setInventoryId(song);
    }

    public void setReviewText(String reviewText) {
        if (reviewText == null || "".equals(reviewText)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Missing review", "Missing review! Please review it"));
        } else {
            this.reviewText = reviewText;
        }
    }
    /**
     * This method add a review to DB
     * Make sure that you are login first
     * 
     * @param login
     * @param song
     * @throws IOException 
     */
    public void addReview(LoginActionBean login, Songs song) throws IOException {
        try {
            System.out.println("This is the login object : " + login);
            System.out.println("This is the boolean : " + login.getLoggedIn());
            if (login.getLoggedIn() != null) {
                if (login.getLoggedIn()) {
                    setReviewParams(login, song);
                    reviewController.create(review);
                    emptyParams();
                } else {
                displayErrorMsg();
                }
            } else {
                displayErrorMsg();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.print(ex);
            Logger.getLogger(ReviewAdminActionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    /**
     * This method return a error msg if the user is not logged in
     */
    private void displayErrorMsg() {
        FacesMessage message = new FacesMessage("Only registered user can review! Please log in!");
        FacesContext.getCurrentInstance().addMessage("reviewForm:submitReviewBtn", message);

    }

    private void emptyParams() {
        this.reviewText = "";
        this.rating = "";
    }
    /**
     * This method return all the review from a specific song that are approved
     * @param reviewList
     * @return 
     */
    public List<Reviews> retriveAllApprovedReviews(List<Reviews> reviewList)
    {
        List<Reviews> list = new ArrayList();
        for(Reviews r: reviewList){
            if(r.getApprovalStatus()){
                list.add(r);
            }
        }
        return list;
    }
    /**
     * This method redirect track page to its album page
     * @param id
     * @return 
     */
    public String redirectToAlbumPage(String id){
        return "albumPage.xhml?albumId=" + id;
    }
}
